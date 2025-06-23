package com.condofacile.controller;

import com.condofacile.dto.AvvisoDTO;
import com.condofacile.service.AvvisoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/condofacile/api/avvisi")
public class AvvisoController {

    @Autowired
    private AvvisoService avvisoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        log.info("Richiesta GET lista avvisi");
        List<AvvisoDTO> avvisi = avvisoService.getAll();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lista avvisi: tot - " + avvisi.size());
        response.put("data", avvisi);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        log.info("Richiesta GET avviso con ID {}", id);
        AvvisoDTO avviso = avvisoService.getById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Avviso trovato");
        response.put("data", avviso);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody AvvisoDTO dto) {
        log.info("Richiesta POST creazione avviso");
        AvvisoDTO created = avvisoService.create(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Avviso creato con successo");
        response.put("data", created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody AvvisoDTO dto) {
        log.info("Richiesta PUT aggiornamento avviso con ID {}", id);
        AvvisoDTO updated = avvisoService.update(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Avviso aggiornato con successo");
        response.put("data", updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        log.info("Richiesta DELETE avviso con ID {}", id);
        avvisoService.delete(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Avviso eliminato con successo");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }
}