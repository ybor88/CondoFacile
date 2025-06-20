package com.condofacile.controller;

import com.condofacile.dto.BollettaDTO;
import com.condofacile.dto.BollettaPdfRequestDTO;
import com.condofacile.service.BollettaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/condofacile/api/bollette")
public class BollettaController {

    @Autowired
    private BollettaService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        log.info("Richiesta GET lista bollette");
        List<BollettaDTO> bollette = service.getAllBollette();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lista bollette: tot - " + bollette.size());
        response.put("data", bollette);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        log.info("Richiesta GET bolletta con ID {}", id);
        BollettaDTO bolletta = service.getBollettaById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Bolletta trovata");
        response.put("data", bolletta);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid BollettaDTO dto) {
        log.info("Richiesta POST creazione bolletta");
        BollettaDTO created = service.createBolletta(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Bolletta creata con successo");
        response.put("data", created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        log.info("Richiesta DELETE bolletta con ID {}", id);
        service.deleteBolletta(id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Bolletta eliminata con successo");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> generaPdf(@RequestBody @Valid BollettaPdfRequestDTO dto) {
        log.info("Richiesta generazione PDF per bolletta");

        byte[] pdfBytes = service.generateBollettaPdf(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("bolletta.pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
