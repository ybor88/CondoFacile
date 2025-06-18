package com.condofacile.service.impl;

import com.condofacile.dto.BollettaDTO;
import com.condofacile.entity.Bolletta;
import com.condofacile.entity.Utente;
import com.condofacile.error.BollettaNotFoundException;
import com.condofacile.repository.BollettaRepository;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.BollettaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BollettaServiceImpl implements BollettaService {

    private final BollettaRepository bollettaRepository;
    private final UtenteRepository utenteRepository;

    @Override
    public List<BollettaDTO> getAllBollette() {
        return bollettaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BollettaDTO getBollettaById(Integer id) {
        Bolletta bolletta = bollettaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bolletta non trovata con ID: " + id));
        return toDTO(bolletta);
    }

    @Override
    public BollettaDTO createBolletta(BollettaDTO dto) {
        Utente utente = utenteRepository.findById(dto.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + dto.getUtenteId()));

        Bolletta.BollettaBuilder builder = Bolletta.builder();
        builder.descrizione(dto.getDescrizione());
        builder.importo(dto.getImporto());
        builder.dataEmissione(dto.getDataEmissione());
        builder.dataScadenza(dto.getDataScadenza());
        builder.pagata(dto.getPagata() != null && dto.getPagata());
        builder.fileUrl(dto.getFileUrl());
        builder.utente(utente);
        Bolletta bolletta = builder
                .build();

        return toDTO(bollettaRepository.save(bolletta));
    }

    @Override
    public void deleteBolletta(Integer id) {
        if (!bollettaRepository.existsById(id)) {
            throw new BollettaNotFoundException(id);
        }
        bollettaRepository.deleteById(id);
    }

    // Helper method per la conversione
    private BollettaDTO toDTO(Bolletta b) {
        return BollettaDTO.builder()
                .id(b.getId())
                .utenteId(b.getUtente().getId())
                .descrizione(b.getDescrizione())
                .importo(b.getImporto())
                .dataEmissione(b.getDataEmissione())
                .dataScadenza(b.getDataScadenza())
                .pagata(b.getPagata())
                .fileUrl(b.getFileUrl())
                .build();
    }
}