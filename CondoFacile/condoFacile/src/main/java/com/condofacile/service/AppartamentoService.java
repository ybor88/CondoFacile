package com.condofacile.service;

import com.condofacile.entity.Appartamento;
import com.condofacile.repository.AppartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppartamentoService {

    @Autowired
    private AppartamentoRepository appartamentoRepository;

    // Aggiungi un nuovo appartamento
    public Appartamento aggiungiAppartamento(Appartamento appartamento) {
        return appartamentoRepository.save(appartamento);
    }

    // Aggiorna un appartamento esistente
    public Appartamento aggiornaAppartamento(Long id, Appartamento dettagliAppartamento) {
        Appartamento appartamento = appartamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appartamento non trovato"));
        appartamento.setNumero(dettagliAppartamento.getNumero());
        appartamento.setPiano(dettagliAppartamento.getPiano());
        appartamento.setSuperficie(dettagliAppartamento.getSuperficie());
        appartamento.setOccupato(dettagliAppartamento.isOccupato());
        return appartamentoRepository.save(appartamento);
    }

    // Recupera tutti gli appartamenti
    public List<Appartamento> ottieniTuttiGliAppartamenti() {
        return appartamentoRepository.findAll();
    }

    // Recupera un appartamento per ID
    public Optional<Appartamento> ottieniAppartamentoPerId(Long id) {
        return appartamentoRepository.findById(id);
    }

    // Elimina un appartamento
    public void eliminaAppartamento(Long id) {
        Appartamento appartamento = appartamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appartamento non trovato"));
        appartamentoRepository.delete(appartamento);
    }
}