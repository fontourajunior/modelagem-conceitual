package br.com.cursomc.cursomodelagemconceitual.repositories;

import br.com.cursomc.cursomodelagemconceitual.domain.Categoria;
import br.com.cursomc.cursomodelagemconceitual.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

//    @Query("SELECT DISTINCT prod FROM Produto prod " +
//            "INNER  JOIN prod.categorias cat " +
//            "WHERE " +
//            "prod.nome LIKE %:nome% " +
//            "AND cat IN :categorias")
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);

}
