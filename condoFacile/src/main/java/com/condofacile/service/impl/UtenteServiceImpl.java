package com.condofacile.service.impl;

import com.condofacile.dto.UtenteDTO;
import com.condofacile.entity.Utente;
import com.condofacile.entity.Utente.Ruolo;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.UtenteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteRepository repository;

    private UtenteDTO toDTO(Utente u) {
        return UtenteDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .cognome(u.getCognome())
                .email(u.getEmail())
                .ruolo(u.getRuolo().name())
                .appartamento(u.getAppartamento())
                .attivo(u.getAttivo())
                .build();
    }

    private Utente toEntity(UtenteDTO dto) {
        Utente hashPlaceholder = Utente.builder()
                //.id(dto.getId()) // ID lo gestiamo manualmente alla creazione
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .email(dto.getEmail())
                .ruolo(Ruolo.valueOf(dto.getRuolo()))
                .appartamento(dto.getAppartamento())
                .attivo(dto.getAttivo() != null ? dto.getAttivo() : true)
                .passwordHash("HASH_PLACEHOLDER") // Gestisci come vuoi
                .build();
        return hashPlaceholder;
    }

    @Override
    public List<UtenteDTO> findAll() {
        log.info("Lettura di tutti gli utenti");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UtenteDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public UtenteDTO create(UtenteDTO dto) {
        log.info("Creazione nuovo utente: {}", dto.getEmail());

        Integer nextId = findNextAvailableId();

        Utente utente = toEntity(dto);
        utente.setId(nextId);

        Utente saved = repository.save(utente);
        return toDTO(saved);
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