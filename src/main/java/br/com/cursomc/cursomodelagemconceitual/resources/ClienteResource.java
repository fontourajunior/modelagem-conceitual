package br.com.cursomc.cursomodelagemconceitual.resources;

import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteDTO;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteNewDTO;
import br.com.cursomc.cursomodelagemconceitual.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cliente> listar(@PathVariable Integer id) {
        return ResponseEntity.ok().body(clienteService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteNewDTO) {
        Cliente cliente= clienteService.insert(clienteService.fromDTO(clienteNewDTO));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
        Cliente cliente = clienteService.fromDTO(clienteDTO);
        cliente.setId(id);
        clienteService.update(cliente);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok().body(buildClienteDTO(clientes));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/page")
    public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                       @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                       @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<ClienteDTO> pageCliente = clienteService.findPage(page, linesPerPage, orderBy, direction).map(ClienteDTO::new);

        return ResponseEntity.ok().body(pageCliente);
    }
 
    private List<ClienteDTO> buildClienteDTO(List<Cliente> clientes) {
        return clientes.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

}
