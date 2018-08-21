package br.com.cursomc.cursomodelagemconceitual.services;

import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.domain.ItemPedido;
import br.com.cursomc.cursomodelagemconceitual.domain.PagamentoComBoleto;
import br.com.cursomc.cursomodelagemconceitual.domain.Pedido;
import br.com.cursomc.cursomodelagemconceitual.domain.enums.EstadoPagamento;
import br.com.cursomc.cursomodelagemconceitual.repositories.ItemPedidoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.PagamentoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.PedidoRepository;
import br.com.cursomc.cursomodelagemconceitual.security.UserSS;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.AuthorizationException;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    public Pedido findOne(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id
        + ", Tipo: " + Pedido.class.getName()));
    }

    @Transactional
    public Pedido insert(Pedido pedido) {
        pedido.setId(null);
        pedido.setInstante(new Date());
        pedido.setCliente(clienteService.findOne(pedido.getCliente().getId()));
        pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        pedido.getPagamento().setPedido(pedido);

        if (pedido.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagamento = (PagamentoComBoleto) pedido.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagamento, pedido.getInstante());
        }

        repository.save(pedido);

        pagamentoRepository.save(pedido.getPagamento());

        for (ItemPedido itemPedido : pedido.getItens()) {
            itemPedido.setDesconto(0.0);
            itemPedido.setProduto(produtoService.findOne(itemPedido.getProduto().getId()));
            itemPedido.setPreco(itemPedido.getProduto().getPreco());
            itemPedido.setPedido(pedido);
        }
        itemPedidoRepository.saveAll(pedido.getItens());

        emailService.sendOrderConfirmationHtmlEMail(pedido);

        return pedido;
    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        UserSS userAuthenticated = UserService.authenticated();
        if (userAuthenticated == null) {
            throw new AuthorizationException("Acesso negado");
        }
        Cliente cliente = clienteService.findOne(userAuthenticated.getId());
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repository.findByCliente(cliente, pageRequest);
    }

}
