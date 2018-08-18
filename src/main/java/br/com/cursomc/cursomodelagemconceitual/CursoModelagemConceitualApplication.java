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

    public static void main(String[] args) {
        SpringApplication.run(CursoModelagemConceitualApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {



    }

}
