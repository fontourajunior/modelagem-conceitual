package br.com.cursomc.cursomodelagemconceitual;

import br.com.cursomc.cursomodelagemconceitual.domain.Categoria;
import br.com.cursomc.cursomodelagemconceitual.domain.Cidade;
import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.domain.Endereco;
import br.com.cursomc.cursomodelagemconceitual.domain.Estado;
import br.com.cursomc.cursomodelagemconceitual.domain.ItemPedido;
import br.com.cursomc.cursomodelagemconceitual.domain.Pagamento;
import br.com.cursomc.cursomodelagemconceitual.domain.PagamentoComBoleto;
import br.com.cursomc.cursomodelagemconceitual.domain.PagamentoComCartao;
import br.com.cursomc.cursomodelagemconceitual.domain.Pedido;
import br.com.cursomc.cursomodelagemconceitual.domain.Produto;
import br.com.cursomc.cursomodelagemconceitual.domain.enums.EstadoPagamento;
import br.com.cursomc.cursomodelagemconceitual.domain.enums.TipoCliente;
import br.com.cursomc.cursomodelagemconceitual.repositories.CategoriaRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.CidadeRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.ClienteRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.EnderecoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.EstadoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.ItemPedidoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.PagamentoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.PedidoRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Array;
import java.net.PortUnreachableException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CursoModelagemConceitualApplication implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public static void main(String[] args) {
        SpringApplication.run(CursoModelagemConceitualApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Categoria categoria1 = new Categoria(null, "Informática");
        Categoria categoria2 = new Categoria(null, "Escritório");

        Produto produto1 = new Produto(null, "Computador", 2000.00);
        Produto produto2 = new Produto(null, "Impressora", 800.00);
        Produto produto3 = new Produto(null, "Mouse", 80.00);

        categoria1.getProdutos().addAll(Arrays.asList(produto1, produto2, produto3));
        categoria2.getProdutos().add(produto2);

        produto1.getCategorias().add(categoria1);
        produto2.getCategorias().addAll(Arrays.asList(categoria1, categoria2));
        produto3.getCategorias().add(categoria1);


        categoriaRepository.saveAll(Arrays.asList(categoria1, categoria2));
        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3));

        Estado mg = new Estado(null, "Minas Gerais");
        Estado sp = new Estado(null, "São Paulo");

        Cidade uberlandia = new Cidade(null, "Uberlândia", mg);
        Cidade saoPaulo = new Cidade(null, "São Paulo", sp);
        Cidade campinas = new Cidade(null, "Campinas", sp);

        mg.getCidades().add(uberlandia);
        sp.getCidades().addAll(Arrays.asList(saoPaulo, campinas));

        estadoRepository.saveAll(Arrays.asList(mg, sp));
        cidadeRepository.saveAll(Arrays.asList(uberlandia, saoPaulo, campinas));

        Cliente cliente1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
        cliente1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

        Endereco end1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cliente1, uberlandia);
        Endereco end2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cliente1, saoPaulo);

        cliente1.getEnderecos().addAll(Arrays.asList(end1, end2));

        clienteRepository.save(cliente1);

        enderecoRepository.saveAll(Arrays.asList(end1, end2));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cliente1, end1);
        Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cliente1, end2);

        Pagamento pagto1 =  new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1,6);
        ped1.setPagamento(pagto1);

        Pagamento pagto2 =  new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("10/20/2017 00:00"), null);
        ped2.setPagamento(pagto2);

        cliente1.getPedido().addAll(Arrays.asList(ped1, ped2));


        pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
        pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));


        ItemPedido ip1 = new ItemPedido(ped1, produto1, 0.00, 1, 2000.00);
        ItemPedido ip2 = new ItemPedido(ped1, produto3, 0.00, 2, 80.00);
        ItemPedido ip3 = new ItemPedido(ped2, produto2, 100.00, 1, 800.00);

        ped1.getItens().addAll(Arrays.asList(ip1, ip2));
        ped2.getItens().add(ip3);

        produto1.getItens().add(ip1);
        produto2.getItens().add(ip3);
        produto3.getItens().add(ip2);

        itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));

    }

}
