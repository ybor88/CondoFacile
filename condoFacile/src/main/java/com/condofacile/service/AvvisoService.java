package com.condofacile.service;

import com.condofacile.dto.AvvisoDTO;

import java.util.List;

public interface AvvisoService {
    AvvisoDTO getById(Integer id);
    List<AvvisoDTO> getAll();
    AvvisoDTO create(AvvisoDTO dto);
    AvvisoDTO update(Integer id, AvvisoDTO dto);
    void delete(Integer id);
}