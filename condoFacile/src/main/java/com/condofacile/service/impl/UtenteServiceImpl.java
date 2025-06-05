package com.condofacile.service.impl;

import com.condofacile.dto.UtenteDTO;
import com.condofacile.entity.Utente;
import com.condofacile.entity.Utente.Ruolo;
import com.condofacile.error.UserCreationException;
import com.condofacile.repository.AppartamentoRepository;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.UtenteService;
import jakarta.persistence.EntityNotFoundException;
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
    @Autowired
    private AppartamentoRepository appartamentoRepository;

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
        // puoi gestire meglio
        if (dto.getAttivo() != null) return Utente.builder()
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .email(dto.getEmail())
                .ruolo(Ruolo.valueOf(dto.getRuolo()))
                .appartamento(dto.getAppartamento())
                .attivo(dto.getAttivo())
                .passwordHash(dto.getPassword())
                .build();
        return Utente.builder()
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .email(dto.getEmail())
                .ruolo(Ruolo.valueOf(dto.getRuolo()))
                .appartamento(dto.getAppartamento())
                .attivo(true)
                .passwordHash(dto.getPassword())
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
            // Password già hashata lato client
            log.info("Creazione nuovo utente: {}", dto.getEmail());

            Integer nextId = findNextAvailableId();
            Utente utente = toEntity(dto);
            utente.setId(nextId);

            // Aggiorna flag occupato sull'appartamento corrispondente
            String codiceAppartamento = utente.getAppartamento();
            if (codiceAppartamento != null && !codiceAppartamento.isEmpty()) {
                int rowsUpdated = appartamentoRepository.setOccupatoTrueByCodice(codiceAppartamento);
                if (rowsUpdated == 0) {
                    log.warn("Appartamento con codice {} non trovato o già occupato", codiceAppartamento);
                } else {
                    log.info("Appartamento con codice {} aggiornato a occupato = true", codiceAppartamento);
                }
            }

            Utente saved = repository.save(utente);
            return toDTO(saved);

        } catch (Exception e) {
            throw new UserCreationException("Errore durante la creazione utente: " + e.getMessage(), e);
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
        log.warn("Richiesta eliminazione utente con ID {}", id);

        // Verifica se l'utente esiste
        if (!repository.existsById(id)) {
            log.error("Utente con ID {} non trovato. Operazione annullata.", id);
            throw new EntityNotFoundException("Utente non trovato con ID: " + id);
        }

        // Recupero codice appartamento prima di eliminare
        String codiceAppartamento = repository.getCodiceAppartamentoById(id);
        if (codiceAppartamento == null) {
            log.warn("Nessun codice appartamento associato all'utente con ID {}", id);
        }

        // Eliminazione utente
        repository.deleteById(id);
        log.info("Utente con ID {} eliminato correttamente", id);

        // Aggiorna occupazione appartamento se esiste
        if (codiceAppartamento != null) {
            int updated = appartamentoRepository.setOccupatoFalseByCodice(codiceAppartamento);
            if (updated > 0) {
                log.info("Appartamento con codice {} marcato come non occupato", codiceAppartamento);
            } else {
                log.warn("Nessun appartamento aggiornato per il codice {}", codiceAppartamento);
            }
        }
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

    @Override
    @Transactional
    public void deleteAllCondomini() {
        log.warn("Eliminazione massiva di tutti gli utenti con ruolo 'condomino'");

        int deletedCount = repository.deleteAllCondomini();
        log.info("Eliminati {} utenti con ruolo 'condomino'", deletedCount);
    }
}