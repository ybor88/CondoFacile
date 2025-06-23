package com.condofacile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvvisoDTO {

    private Integer id;
    private String titolo;
    private String messaggio;
    private LocalDateTime dataPubblicazione;
    private Boolean soloPersonale;
    private Integer destinatarioId; // solo l'id dell'utente destinatario
}
