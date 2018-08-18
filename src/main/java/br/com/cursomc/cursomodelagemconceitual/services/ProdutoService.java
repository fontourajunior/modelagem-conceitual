package br.com.cursomc.cursomodelagemconceitual.services;

import br.com.cursomc.cursomodelagemconceitual.domain.Categoria;
import br.com.cursomc.cursomodelagemconceitual.domain.Produto;
import br.com.cursomc.cursomodelagemconceitual.repositories.CategoriaRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.ProdutoRepository;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Produto findOne(Integer id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id
        + ", Tipo: " + Produto.class.getName()));
    }

    public Page<Produto> search(String nome, List<Integer> categoriasIds, Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        List<Categoria> categorias = categoriaRepository.findAllById(categoriasIds);
        return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
    }

}
