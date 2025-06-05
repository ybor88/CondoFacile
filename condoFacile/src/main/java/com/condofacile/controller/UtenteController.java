package com.condofacile.controller;

import com.condofacile.dto.UtenteDTO;
import com.condofacile.service.UtenteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/condofacile/api/utenti")
public class UtenteController {

    @Autowired
    private UtenteService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        log.info("Richiesta GET lista utenti");
        List<UtenteDTO> utenti = service.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lista utenti: tot - " + utenti.size());
        response.put("data", utenti);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        UtenteDTO utente = service.findById(id);
        Map<String, Object> response = new HashMap<>();

        if (utente == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "Utente non trovato con id: " + id);
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("message", "Utente trovato");
        response.put("data", utente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid UtenteDTO dto) {
        UtenteDTO created = service.create(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Utente creato con successo");
        response.put("data", created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody @Valid UtenteDTO dto) {
        UtenteDTO updated = service.update(id, dto);
        Map<String, Object> response = new HashMap<>();

        if (updated == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "Utente non trovato con id: " + id);
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("message", "Utente aggiornato con successo");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        service.delete(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NO_CONTENT.value());
        response.put("message", "Utente eliminato con successo");
        response.put("data", null);

        // anche se 204 non ha body, con questa struttura ritorniamo 200 con messaggio
        return ResponseEntity.ok(response);
    }
}