package com.condofacile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BollettaPdfRequestDTO {
    private String descrizione;
    private BigDecimal importo;
    private LocalDate dataEmissione;
    private LocalDate dataScadenza;
    private Boolean pagata;
    private String utenteId; // usata solo in fase di creazione
}
