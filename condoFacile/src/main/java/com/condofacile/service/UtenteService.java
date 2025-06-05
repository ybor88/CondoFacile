package com.condofacile.service;

import com.condofacile.dto.UtenteDTO;

import java.util.List;

public interface UtenteService {
    List<UtenteDTO> findAll();
    UtenteDTO findById(Integer id);
    UtenteDTO create(UtenteDTO dto);
    UtenteDTO update(Integer id, UtenteDTO dto);
    void delete(Integer id);
    void deleteAllCondomini();
    boolean validateLogin(String email, String password);
}