package com.condofacile.service;

import com.condofacile.dto.BollettaDTO;
import com.condofacile.dto.BollettaPdfRequestDTO;

import java.util.List;

public interface BollettaService {

    List<BollettaDTO> getAllBollette();

    BollettaDTO getBollettaById(Integer id);

    BollettaDTO createBolletta(BollettaDTO dto);

    void deleteBolletta(Integer id);

    byte[] generateBollettaPdf(BollettaPdfRequestDTO dto);
}