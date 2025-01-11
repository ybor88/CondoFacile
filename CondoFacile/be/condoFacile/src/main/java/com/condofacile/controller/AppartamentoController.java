package com.condofacile.controller;

import com.condofacile.entity.Appartamento;
import com.condofacile.service.AppartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appartamenti")
public class AppartamentoController {

    @Autowired
    private AppartamentoService appartamentoService;

    // Aggiungi un nuovo appartamento
    @PostMapping
    public ResponseEntity<Appartamento> aggiungiAppartamento(@RequestBody Appartamento appartamento) {
        Appartamento nuovoAppartamento = appartamentoService.aggiungiAppartamento(appartamento);
        return new ResponseEntity<>(nuovoAppartamento, HttpStatus.CREATED);
    }

    // Ottieni tutti gli appartamenti
    @GetMapping
    public ResponseEntity<List<Appartamento>> ottieniTuttiGliAppartamenti() {
        List<Appartamento> appartamenti = appartamentoService.ottieniTuttiGliAppartamenti();
        return new ResponseEntity<>(appartamenti, HttpStatus.OK);
    }

    // Ottieni un appartamento per ID
    @GetMapping("/{id}")
    public ResponseEntity<Appartamento> ottieniAppartamentoPerId(@PathVariable Long id) {
        Optional<Appartamento> appartamento = appartamentoService.ottieniAppartamentoPerId(id);
        return appartamento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Aggiorna un appartamento
    @PutMapping("/{id}")
    public ResponseEntity<Appartamento> aggiornaAppartamento(@PathVariable Long id, @RequestBody Appartamento appartamento) {
        try {
            Appartamento appartamentoAggiornato = appartamentoService.aggiornaAppartamento(id, appartamento);
            return new ResponseEntity<>(appartamentoAggiornato, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Elimina un appartamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaAppartamento(@PathVariable Long id) {
        try {
            appartamentoService.eliminaAppartamento(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}