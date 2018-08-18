package br.com.cursomc.cursomodelagemconceitual.services.validation;

import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.domain.enums.TipoCliente;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteNewDTO;
import br.com.cursomc.cursomodelagemconceitual.repositories.ClienteRepository;
import br.com.cursomc.cursomodelagemconceitual.resources.exception.FieldMessage;
import br.com.cursomc.cursomodelagemconceitual.services.validation.Utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteInsert constraintAnnotation) {

    }

    @Override
    public boolean isValid(ClienteNewDTO clienteNewDTO, ConstraintValidatorContext context) {
        List<FieldMessage> fieldMessages = new ArrayList<>();

        if (validTipoPessoa(clienteNewDTO, TipoCliente.PESSOAFISICA) && !isValidCPF(clienteNewDTO)) {
            fieldMessages.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }

        if (validTipoPessoa(clienteNewDTO, TipoCliente.PESSOAJURIDICA) && !isValidCNPJ(clienteNewDTO)) {
            fieldMessages.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        if (emailExists(clienteNewDTO)) {
            fieldMessages.add(new FieldMessage("email", "E-mail já existente"));
        }

        for (FieldMessage e : fieldMessages) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
        }

        return fieldMessages.isEmpty();
    }

    private Boolean emailExists(ClienteNewDTO clienteNewDTO) {
        return clienteRepository.countAllByEmail(clienteNewDTO.getEmail()) > 0;
    }

    private boolean isValidCNPJ(ClienteNewDTO clienteNewDTO) {
        return BR.isValidCNPJ(clienteNewDTO.getCpfOuCnpj());
    }

    private boolean isValidCPF(ClienteNewDTO clienteNewDTO) {
        return BR.isValidCPF(clienteNewDTO.getCpfOuCnpj());
    }

    private boolean validTipoPessoa(ClienteNewDTO clienteNewDTO, TipoCliente pessoafisica) {
        return clienteNewDTO.getTipo().equals(pessoafisica.getCod());
    }
}
