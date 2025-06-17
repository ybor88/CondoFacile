package com.condofacile.service.impl;

import com.condofacile.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("condofaciletest@gmail.com");

            // Corpo HTML dell'email
            String htmlContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9;'>"
                    + "<img src='cid:logoImage' style='max-width: 150px; margin-bottom: 20px;' alt='CondoFacile Logo' />"
                    + "<h2 style='color: #2c3e50;'>Benvenuto da CondoFacile!</h2>"
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