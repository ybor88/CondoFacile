package com.condofacile.dto;

public class AppartamentoDTO {
    private String codice;
    private boolean occupato;

    // Costruttori
    public AppartamentoDTO() {
    }

    public AppartamentoDTO(String codice, boolean occupato) {
        this.codice = codice;
        this.occupato = occupato;
    }

    // Getters e Setters
    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public boolean isOccupato() {
        return occupato;
    }

    public void setOccupato(boolean occupato) {
        this.occupato = occupato;
    }
}