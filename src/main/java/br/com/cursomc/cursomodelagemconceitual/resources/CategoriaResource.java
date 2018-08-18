package br.com.cursomc.cursomodelagemconceitual.resources;

import br.com.cursomc.cursomodelagemconceitual.domain.Categoria;
import br.com.cursomc.cursomodelagemconceitual.dto.CategoriaDTO;
import br.com.cursomc.cursomodelagemconceitual.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Categoria> findOne(@PathVariable Integer id) {
        return ResponseEntity.ok().body(categoriaService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaService.insert(categoriaService.fromDTO(categoriaDTO));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer id) {
        Categoria categoria = categoriaService.fromDTO(categoriaDTO);
        categoria.setId(id);
        categoriaService.update(categoria);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok().body(buildCategoriaDTO(categorias));
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                       @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                       @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<CategoriaDTO> pageCategoria = categoriaService.findPage(page, linesPerPage, orderBy, direction).map(CategoriaDTO::new);

        return ResponseEntity.ok().body(pageCategoria);
    }

    private List<CategoriaDTO> buildCategoriaDTO(List<Categoria> categorias) {
        return categorias.stream().map(CategoriaDTO::new).collect(Collectors.toList());
    }

}
