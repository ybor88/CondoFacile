package com.condofacile.service.impl;

import com.condofacile.dto.UtenteDTO;
import com.condofacile.entity.Utente;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return Utente.builder()
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .email(dto.getEmail())
                .ruolo(Utente.Ruolo.valueOf(dto.getRuolo()))
                .appartamento(dto.getAppartamento())
                .attivo(dto.getAttivo() != null ? dto.getAttivo() : true)
                .passwordHash("PLACEHOLDER") // da gestire in sicurezza
                .build();
    }

    @Override
    public List<UtenteDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public UtenteDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO).orElseThrow();
    }


    @Override
    public UtenteDTO create(UtenteDTO dto) {
        Utente utente = toEntity(dto);
        return toDTO(repository.save(utente));
    }

    @Override
    public UtenteDTO update(Integer id, UtenteDTO dto) {
        Utente u = repository.findById(id).orElseThrow();
        u.setNome(dto.getNome());
        u.setCognome(dto.getCognome());
        u.setEmail(dto.getEmail());
        u.setRuolo(Utente.Ruolo.valueOf(dto.getRuolo()));
        u.setAppartamento(dto.getAppartamento());
        u.setAttivo(dto.getAttivo());
        return toDTO(repository.save(u));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}