package com.condofacile.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;
    private String appartamento;
    private Boolean attivo;
}