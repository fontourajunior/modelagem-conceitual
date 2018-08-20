package br.com.cursomc.cursomodelagemconceitual.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

public class MockEmailService extends AbstractEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(SimpleMailMessage message) {
        LOG.info("Simulando envio de e-mail...");
        LOG.info(message.toString());
        LOG.info("E-mail enviado");
    }

    @Override
    public void sendHtmlMail(MimeMessage message) {
        LOG.info("Simulando envio de e-mail HTML...");
        LOG.info(message.toString());
        LOG.info("E-mail enviado");
    }

}
