package br.com.cursomc.cursomodelagemconceitual.resources;

import br.com.cursomc.cursomodelagemconceitual.domain.Produto;
import br.com.cursomc.cursomodelagemconceitual.dto.ProdutoDTO;
import br.com.cursomc.cursomodelagemconceitual.resources.utils.URL;
import br.com.cursomc.cursomodelagemconceitual.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Produto> findOne(@PathVariable Integer id) {
        return ResponseEntity.ok().body(produtoService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> findPage(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categorias", defaultValue = "") String categorias,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        String nomeDecoded = URL.decodeParam(nome);
        List<Integer> categoriaIds = URL.decodeIntList(categorias);

        Page<ProdutoDTO> pageProdDTOs = produtoService.search(nomeDecoded, categoriaIds, page, linesPerPage, orderBy, direction)
                .map(ProdutoDTO::new);
        return ResponseEntity.ok().body(pageProdDTOs);
    }

}
