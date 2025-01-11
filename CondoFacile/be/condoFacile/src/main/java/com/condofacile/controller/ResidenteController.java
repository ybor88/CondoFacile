package com.condofacile.controller;

import com.condofacile.entity.Residente;
import com.condofacile.repository.ResidenteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residenti")
public class ResidenteController {

    private final ResidenteRepository residenteRepository;

    public ResidenteController(ResidenteRepository residenteRepository) {
        this.residenteRepository = residenteRepository;
    }

    @GetMapping
    public List<Residente> getAllResidenti() {
        return residenteRepository.findAll();
    }

    @PostMapping
    public Residente createResidente(@RequestBody Residente residente) {
        return residenteRepository.save(residente);
    }

    @DeleteMapping("/{id}")
    public void deleteResidente(@PathVariable Long id) {
        residenteRepository.deleteById(id);
    }
}
