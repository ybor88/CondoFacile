package com.condofacile.service;

import com.condofacile.dto.BollettaDTO;

import java.util.List;

public interface BollettaService {

    List<BollettaDTO> getAllBollette();

    BollettaDTO getBollettaById(Integer id);

    BollettaDTO createBolletta(BollettaDTO dto);

    void deleteBolletta(Integer id);
}