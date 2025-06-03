package com.condofacile.service.impl;

import com.condofacile.dto.UtenteDTO;
import com.condofacile.entity.Utente;
import com.condofacile.entity.Utente.Ruolo;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.UtenteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteRepository repository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UtenteDTO toDTO(Utente u) {
        return UtenteDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .cognome(u.getCognome())
                .email(u.getEmail())
                .ruolo(u.getRuolo().name())
                .appartamento(u.getAppartamento())
                .attivo(u.getAttivo())
                .password("Password non mostrata per motivi di sicurezza")
                .build();
    }

    private Utente toEntity(UtenteDTO dto) {
        return Utente.builder()
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .email(dto.getEmail())
                .ruolo(Ruolo.valueOf(dto.getRuolo()))
                .appartamento(dto.getAppartamento())
                .attivo(dto.getAttivo() != null ? dto.getAttivo() : true)
                .passwordHash(dto.getPassword()) // puoi gestire meglio
                .build();
    }

    @Override
    public List<UtenteDTO> findAll() {
        log.info("Lettura di tutti gli utenti");
        List<UtenteDTO> list = new ArrayList<>();
        for (Utente utente : repository.findAll()) {
            UtenteDTO dto = toDTO(utente);
            list.add(dto);
        }
        return list;
    }

    @Override
    public UtenteDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO).orElse(null);
    }

    public UtenteDTO create(UtenteDTO dto) {
        try {
            // Ora la password è SHA-256 hash lato client (hex string)
            String passwordFromClient = dto.getPassword();
            log.info("Creazione nuovo utente: {}", dto.getEmail());
            Integer nextId = findNextAvailableId();
            Utente utente = toEntity(dto);
            utente.setId(nextId);
            Utente saved = repository.save(utente);

            return toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione utente: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UtenteDTO update(Integer id, UtenteDTO dto) {
        return repository.findById(id).map(existing -> {
            log.info("Aggiornamento utente con ID {}", id);
            existing.setNome(dto.getNome());
            existing.setCognome(dto.getCognome());
            existing.setEmail(dto.getEmail());
            existing.setRuolo(Ruolo.valueOf(dto.getRuolo()));
            existing.setAppartamento(dto.getAppartamento());
            existing.setAttivo(dto.getAttivo());

            // Se password presente e non vuota => aggiorna hash
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            }

            return toDTO(repository.save(existing));
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.warn("Eliminazione utente con ID {}", id);
        repository.deleteById(id);
    }

    /**
     * Trova il più piccolo ID intero positivo disponibile non usato
     * nella tabella utenti.
     */
    private Integer findNextAvailableId() {
        List<Integer> ids = repository.findAll().stream()
                .map(Utente::getId)
                .sorted()
                .toList();

        int expectedId = 1;
        for (Integer id : ids) {
            if (id > expectedId) {
                // Trovato un "buco" nella sequenza
                return expectedId;
            } else if (id == expectedId) {
                expectedId++;
            }
        }
        return expectedId;
    }
}