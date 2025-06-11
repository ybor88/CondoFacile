package com.condofacile.service.impl;

import com.condofacile.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("condofaciletest@gmail.com");

            mailSender.send(message);
            log.info("Email inviata con successo a {}", to);
            return true;
        } catch (Exception e) {
            log.error("Errore durante l'invio dell'email: {}", e.getMessage(), e);
            return false;
        }
    }
}