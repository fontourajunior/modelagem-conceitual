package br.com.cursomc.cursomodelagemconceitual.services;

import br.com.cursomc.cursomodelagemconceitual.domain.Categoria;
import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.dto.CategoriaDTO;
import br.com.cursomc.cursomodelagemconceitual.repositories.CategoriaRepository;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.DataIntegrityException;
import br.com.cursomc.cursomodelagemconceitual.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria findOne(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id
        + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria categoria) {
        categoria.setId(null);
        return repository.save(categoria);
    }

    public Categoria update(Categoria categoria) {
        Categoria categoriaToUpdate = findOne(categoria.getId());
        updateData(categoriaToUpdate, categoria);
        return repository.save(categoriaToUpdate);
    }

    private void updateData(Categoria categoriaToUpdate, Categoria categoria) {
        categoriaToUpdate.setNome(categoria.getNome());
    }

    public void delete(Integer id) {
        findOne(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos.");
        }
    }

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repository.findAll(pageRequest);
    }

    public Categoria fromDTO(CategoriaDTO categoriaDTO) {
        return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
    }
}
