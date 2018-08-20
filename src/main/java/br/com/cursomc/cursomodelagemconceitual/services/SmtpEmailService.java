package br.com.cursomc.cursomodelagemconceitual.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

public class SmtpEmailService extends AbstractEmailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

    @Override
    public void sendMail(SimpleMailMessage message) {
        LOG.info("Enviando e-mail...");
        mailSender.send(message);
        LOG.info("E-mail enviado");
    }

    @Override
    public void sendHtmlMail(MimeMessage message) {
        LOG.info("Enviando e-mail...");
        javaMailSender.send(message);
        LOG.info("E-mail enviado");
    }
}
