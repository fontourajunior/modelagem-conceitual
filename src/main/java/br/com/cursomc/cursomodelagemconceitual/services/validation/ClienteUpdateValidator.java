package br.com.cursomc.cursomodelagemconceitual.services.validation;

import br.com.cursomc.cursomodelagemconceitual.domain.enums.TipoCliente;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteDTO;
import br.com.cursomc.cursomodelagemconceitual.dto.ClienteNewDTO;
import br.com.cursomc.cursomodelagemconceitual.repositories.ClienteRepository;
import br.com.cursomc.cursomodelagemconceitual.resources.exception.FieldMessage;
import br.com.cursomc.cursomodelagemconceitual.services.validation.Utils.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteUpdate constraintAnnotation) {

    }

    @Override
    public boolean isValid(ClienteDTO clienteDTO, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        List<FieldMessage> fieldMessages = new ArrayList<>();

        if (emailExists(clienteDTO, uriId)) {
            fieldMessages.add(new FieldMessage("email", "E-mail já existente"));
        }

        for (FieldMessage e : fieldMessages) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
        }

        return fieldMessages.isEmpty();
    }

    private Boolean emailExists(ClienteDTO clienteDTO, Integer id) {
        return clienteRepository.countAllByEmailAndIdNot(clienteDTO.getEmail(), id) > 0;
    }

}
