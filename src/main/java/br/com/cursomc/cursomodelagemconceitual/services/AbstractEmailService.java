package br.com.cursomc.cursomodelagemconceitual.services;

import br.com.cursomc.cursomodelagemconceitual.domain.Cliente;
import br.com.cursomc.cursomodelagemconceitual.domain.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendOrderConfirmationEmail(Pedido pedido) {
        SimpleMailMessage messageEmail = prepareSimpleMailMessageFromPedido(pedido);
        sendMail(messageEmail);
    }

    @Override
    public void sendOrderConfirmationHtmlEMail(Pedido pedido) {
        try {
            MimeMessage mm = prepareMimeMessageFromPedido(pedido);
            sendHtmlMail(mm);
        } catch (MessagingException e) {
            sendOrderConfirmationEmail(pedido);
        }
    }

    protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setTo(pedido.getCliente().getEmail());
        mmh.setFrom(sender);
        mmh.setSubject("Pedido Confirmado! Código: " + pedido.getId());
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(htmlFromTemplatePedido(pedido), true);
        return mimeMessage;

    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(pedido.getCliente().getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("Pedido Confirmado! Código: " + pedido.getId());
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(pedido.toString());

        return simpleMailMessage;
    }

    protected String htmlFromTemplatePedido(Pedido pedido) {
        Context context = new Context();
        context.setVariable("pedido", pedido);
        return templateEngine.process("email/confirmacaoPedido", context);
    }

    @Override
    public void sendNewPasswordEmail(Cliente cliente, String newPassword) {
        SimpleMailMessage messageEmail = preparePasswordEmail(cliente, newPassword);
        sendMail(messageEmail);
    }

    protected SimpleMailMessage preparePasswordEmail(Cliente cliente, String newPassword) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(cliente.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("Solicitação de nova senha");
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText("Nova senha: " + newPassword);

        return simpleMailMessage;
    }


}
