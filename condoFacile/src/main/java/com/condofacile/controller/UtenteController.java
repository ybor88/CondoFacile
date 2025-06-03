package com.condofacile.controller;

import com.condofacile.dto.UtenteDTO;
import com.condofacile.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UtenteService service;

    @GetMapping
    public List<UtenteDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UtenteDTO getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public UtenteDTO create(@RequestBody UtenteDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UtenteDTO update(@PathVariable Integer id, @RequestBody UtenteDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}