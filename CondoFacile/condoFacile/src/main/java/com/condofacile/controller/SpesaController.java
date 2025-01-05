package com.condofacile.controller;

import com.condofacile.entity.Spesa;
import com.condofacile.service.SpesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spese")
public class SpesaController {

    @Autowired
    private SpesaService spesaService;

    // Aggiungi una nuova spesa
    @PostMapping
    public ResponseEntity<Spesa> aggiungiSpesa(@RequestBody Spesa spesa) {
        Spesa nuovaSpesa = spesaService.aggiungiSpesa(spesa);
        return new ResponseEntity<>(nuovaSpesa, HttpStatus.CREATED);
    }

    // Ottieni tutte le spese
    @GetMapping
    public ResponseEntity<List<Spesa>> ottieniTutteLeSpese() {
        List<Spesa> spese = spesaService.ottieniTutteLeSpese();
        return new ResponseEntity<>(spese, HttpStatus.OK);
    }

    // Ottieni una spesa per ID
    @GetMapping("/{id}")
    public ResponseEntity<Spesa> ottieniSpesaPerId(@PathVariable Long id) {
        Optional<Spesa> spesa = spesaService.ottieniSpesaPerId(id);
        return spesa.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Aggiorna una spesa
    @PutMapping("/{id}")
    public ResponseEntity<Spesa> aggiornaSpesa(@PathVariable Long id, @RequestBody Spesa spesa) {
        try {
            Spesa spesaAggiornata = spesaService.aggiornaSpesa(id, spesa);
            return new ResponseEntity<>(spesaAggiornata, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Elimina una spesa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaSpesa(@PathVariable Long id) {
        try {
            spesaService.eliminaSpesa(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}