package br.com.cursomc.cursomodelagemconceitual.services;

import br.com.cursomc.cursomodelagemconceitual.domain.Cidade;
import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.domain.Endereco;
import br.com.cursomc.cursomodelagemconceitual.domain.enums.TipoCliente;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteDTO;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteNewDTO;
import br.com.cursomc.cursomodelagemconceitual.repositories.ClienteRepository;
import br.com.cursomc.cursomodelagemconceitual.repositories.EnderecoRepository;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.DataIntegrityException;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente findOne(Integer id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                                                                                             + ", Tipo: " + Cliente.class.getName()));
    }

    @Transactional
    public Cliente insert(Cliente cliente) {
        cliente.setId(null);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        enderecoRepository.saveAll(clienteSalvo.getEnderecos());
        return clienteSalvo;
    }

    public Cliente update(Cliente cliente) {
        Cliente clienteToUpdate = findOne(cliente.getId());
        updateData(clienteToUpdate, cliente);
        return clienteRepository.save(clienteToUpdate);
    }

    private void updateData(Cliente clienteToUpdate, Cliente cliente) {
        clienteToUpdate.setNome(cliente.getNome());
        clienteToUpdate.setEmail(cliente.getEmail());
    }

    public void delete(Integer id) {
        findOne(id);
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir porque existe pedidos relacionados.");
        }
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clienteRepository.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO clienteDTO) {
        return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
    }

    public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
        Cliente cliente = buildCliente(clienteNewDTO);
        Endereco endereco = buildEndereco(clienteNewDTO, cliente);

        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(clienteNewDTO.getTelefone1());
        addFoneIfExists(clienteNewDTO, cliente);
        return cliente;
    }

    private void addFoneIfExists(ClienteNewDTO clienteNewDTO, Cliente cliente) {
        if (clienteNewDTO.getTelefone2() != null) {
            cliente.getTelefones().add(clienteNewDTO.getTelefone2());
        }

        if (clienteNewDTO.getTelefone3() != null) {
            cliente.getTelefones().add(clienteNewDTO.getTelefone3());
        }
    }

    private Endereco buildEndereco(ClienteNewDTO clienteNewDTO, Cliente cliente) {
        return new Endereco(null,
                            clienteNewDTO.getLogradouro(),
                            clienteNewDTO.getNumero(),
                            clienteNewDTO.getComplemento(),
                            clienteNewDTO.getBairro(),
                            clienteNewDTO.getCep(),
                            cliente,
                            new Cidade(clienteNewDTO.getCidadeId(), null, null));
    }

    private Cliente buildCliente(ClienteNewDTO clienteNewDTO) {
        return new Cliente(null,
                           clienteNewDTO.getNome(),
                           clienteNewDTO.getEmail(),
                           clienteNewDTO.getCpfOuCnpj(),
                           TipoCliente.toEnum(clienteNewDTO.getTipo()));
    }



}
