package br.com.cursomc.cursomodelagemconceitual.repositories;

import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);

}
