package com.condofacile.service;

import com.condofacile.dto.AppartamentoDTO;

import java.util.List;

public interface RegisterService {
    List<AppartamentoDTO>  getAvailableAppartamenti();
}