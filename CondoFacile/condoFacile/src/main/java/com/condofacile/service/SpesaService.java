package com.condofacile.service;

import com.condofacile.entity.Spesa;
import com.condofacile.repository.SpesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpesaService {

    @Autowired
    private SpesaRepository spesaRepository;

    // Aggiungi una nuova spesa
    public Spesa aggiungiSpesa(Spesa spesa) {
        return spesaRepository.save(spesa);
    }

    // Aggiorna una spesa esistente
    public Spesa aggiornaSpesa(Long id, Spesa dettagliSpesa) {
        Spesa spesa = spesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spesa non trovata"));
        spesa.setCategoria(dettagliSpesa.getCategoria());
        spesa.setImporto(dettagliSpesa.getImporto());
        return spesaRepository.save(spesa);
    }

    // Recupera tutte le spese
    public List<Spesa> ottieniTutteLeSpese() {
        return spesaRepository.findAll();
    }

    // Recupera una spesa per ID
    public Optional<Spesa> ottieniSpesaPerId(Long id) {
        return spesaRepository.findById(id);
    }

    // Elimina una spesa
    public void eliminaSpesa(Long id) {
        Spesa spesa = spesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spesa non trovata"));
        spesaRepository.delete(spesa);
    }
}