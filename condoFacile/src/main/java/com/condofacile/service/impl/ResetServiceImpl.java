package com.condofacile.service.impl;

import com.condofacile.service.EmailService;
import com.condofacile.service.ResetService;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.entity.Utente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ResetServiceImpl implements ResetService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public boolean sendResetLink(String email, String link) {
        String message = "Clicca sul link per reimpostare la tua password:\n" + link;
        return emailService.sendEmail(email, "Recupero Password CondoFacile", message);
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        try {
            Optional<Utente> utente = utenteRepository.findByEmail(email);
            if (utente.isEmpty()) return false;

            utente.get().setPasswordHash(newPassword);
            utenteRepository.save(utente.get());
            log.info("Password aggiornata per utente {}", email);
            return true;
        } catch (Exception e) {
            log.error("Errore aggiornamento password: {}", e.getMessage(), e);
            return false;
        }
    }
}