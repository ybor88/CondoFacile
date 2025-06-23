package com.condofacile.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BollettaDTO {
    private Integer id;
    private Integer utenteId;
    private String descrizione;
    private BigDecimal importo;
    private LocalDate dataEmissione;
    private LocalDate dataScadenza;
    private Boolean pagata;
}