package com.condofacile.service.impl;

import com.condofacile.dto.AvvisoDTO;
import com.condofacile.entity.Avviso;
import com.condofacile.entity.Utente;
import com.condofacile.error.AvvisoNotFoundException;
import com.condofacile.repository.AvvisoRepository;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.AvvisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AvvisoServiceImpl implements AvvisoService {

    @Autowired
    private AvvisoRepository avvisoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public AvvisoDTO getById(Integer id) {
        Avviso avviso = avvisoRepository.findById(id)
                .orElseThrow(() -> new AvvisoNotFoundException(id));
        return convertToDto(avviso);
    }

    @Override
    public List<AvvisoDTO> getAll() {
        return avvisoRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AvvisoDTO create(AvvisoDTO dto) {
        Utente destinatario = null;
        if (dto.getDestinatarioId() != null) {
            destinatario = utenteRepository.findById(dto.getDestinatarioId())
                    .orElseThrow(() -> new RuntimeException("Utente destinatario non trovato"));
        }

        Avviso avviso = new Avviso();
        avviso.setTitolo(dto.getTitolo());
        avviso.setMessaggio(dto.getMessaggio());
        avviso.setSoloPersonale(dto.getSoloPersonale());
        avviso.setDataPubblicazione(dto.getDataPubblicazione());
        avviso.setDestinatario(destinatario);

        Avviso saved = avvisoRepository.save(avviso);
        return convertToDto(saved);
    }

    @Override
    public AvvisoDTO update(Integer id, AvvisoDTO dto) {
        Avviso avviso = avvisoRepository.findById(id)
                .orElseThrow(() -> new AvvisoNotFoundException(id));

        avviso.setTitolo(dto.getTitolo());
        avviso.setMessaggio(dto.getMessaggio());
        avviso.setSoloPersonale(dto.getSoloPersonale());
        avviso.setDataPubblicazione(dto.getDataPubblicazione());

        if (dto.getDestinatarioId() != null) {
            Utente destinatario = utenteRepository.findById(dto.getDestinatarioId())
                    .orElseThrow(() -> new RuntimeException("Utente destinatario non trovato"));
            avviso.setDestinatario(destinatario);
        } else {
            avviso.setDestinatario(null);
        }

        Avviso updated = avvisoRepository.save(avviso);
        return convertToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!avvisoRepository.existsById(id)) {
            throw new AvvisoNotFoundException(id);
        }
        avvisoRepository.deleteById(id);
    }

    // 🔽 Metodo privato per conversione DTO
    private AvvisoDTO convertToDto(Avviso avviso) {
        AvvisoDTO dto = new AvvisoDTO();
        dto.setId(avviso.getId());
        dto.setTitolo(avviso.getTitolo());
        dto.setMessaggio(avviso.getMessaggio());
        dto.setDataPubblicazione(avviso.getDataPubblicazione());
        dto.setSoloPersonale(avviso.getSoloPersonale());
        if (avviso.getDestinatario() != null) {
            dto.setDestinatarioId(avviso.getDestinatario().getId());
        }
        return dto;
    }
}
