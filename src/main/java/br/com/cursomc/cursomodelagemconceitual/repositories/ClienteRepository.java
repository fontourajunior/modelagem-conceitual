package br.com.cursomc.cursomodelagemconceitual.repositories;

import br.com.cursomc.cursomodelagemconceitual.domain.Categoria;
import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Transactional(readOnly = true)
    Integer countAllByEmail(String email);

    @Transactional(readOnly = true)
    Integer countAllByEmailAndIdNot(String email, Integer id);

    @Transactional(readOnly = true)
    Cliente findByEmail(String email);
}
