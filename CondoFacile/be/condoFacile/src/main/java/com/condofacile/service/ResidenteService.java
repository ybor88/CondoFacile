package com.condofacile.service;

import com.condofacile.entity.Residente;
import com.condofacile.repository.ResidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidenteService {

    @Autowired
    private ResidenteRepository residenteRepository;

    // Aggiungi un nuovo residente
    public Residente addResident(Residente residente) {
        return residenteRepository.save(residente);
    }

    // Aggiorna un residente esistente
    public Residente updateResidente(Long id, Residente residenteDetails) {
        Residente residente = residenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Residente not found"));
        residente.setNome(residenteDetails.getNome());
        residente.setAppartamentoId(residenteDetails.getAppartamentoId());
        residente.setContatti(residenteDetails.getContatti());
        residente.setRuolo(residenteDetails.getRuolo());
        return residenteRepository.save(residente);
    }

    // Recupera tutti i residenti
    public List<Residente> getAllResidenti() {
        return residenteRepository.findAll();
    }

    // Recupera un residente per ID
    public Optional<Residente> getResidenteById(Long id) {
        return residenteRepository.findById(id);
    }

    // Elimina un residente
    public void deleteResidente(Long id) {
        Residente residente = residenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Residente not found"));
        residenteRepository.delete(residente);
    }
}