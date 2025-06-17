package com.condofacile.service.impl;

import com.condofacile.entity.Utente;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public boolean sendEmail(String to, String subject, String text) {
        try {
            // Recupera l'utente tramite email
            Optional<Utente> utenteOpt = utenteRepository.findByEmail(to);
            String fullName = utenteOpt
                    .map(u -> Stream.of(u.getNome(), u.getCognome())
                            .map(s -> Arrays.stream(s.trim().toLowerCase().split("\\s+"))
                                    .map(word -> word.isEmpty() ? "" : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                                    .collect(Collectors.joining(" ")))
                            .collect(Collectors.joining(" ")))
                    .orElse("Gentile Utente");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("condofaciletest@gmail.com");

            // Corpo HTML dell'email con nome e cognome
            String htmlContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9;'>"
                    + "<img src='cid:logoImage' style='max-width: 150px; margin-bottom: 20px;' alt='CondoFacile Logo' />"
                    + "<h2 style='color: #2c3e50;'>Caro " + fullName + ",</h2>"
                    + "<p style='font-size: 14px; color: #333;'>" + text + "</p>"
                    + "<hr style='margin-top: 30px;'>"
                    + "<p style='font-size: 12px; color: #888;'>Questa è una email automatica. Non rispondere a questo messaggio.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(htmlContent, true);

            // Inserimento immagine inline (logo)
            ClassPathResource logo = new ClassPathResource("/static/img/condofacileLogo.png");
            helper.addInline("logoImage", logo);

            mailSender.send(message);
            log.info("Email HTML inviata con successo a {}", to);
            return true;
        } catch (Exception e) {
            log.error("Errore durante l'invio dell'email HTML: {}", e.getMessage(), e);
            return false;
        }
    }
}