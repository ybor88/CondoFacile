package com.condofacile.service.impl;

import com.condofacile.dto.AppartamentoDTO;
import com.condofacile.entity.Appartamento;
import com.condofacile.repository.AppartamentoRepository;
import com.condofacile.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private AppartamentoRepository appartamentoRepository;

    @Override
    public List<AppartamentoDTO> getAvailableAppartamenti() {
        List<Appartamento> appartamenti = appartamentoRepository.findAvailableAppartamenti();

        return appartamenti.stream()
                .map(a -> new AppartamentoDTO(a.getCodice(), a.getOccupato()))
                .collect(Collectors.toList());
    }
}