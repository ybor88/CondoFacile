package com.condofacile.error;

public class UtenteNotFoundException extends RuntimeException {
    public UtenteNotFoundException(Integer id) {
        super("Utente non trovato con ID: " + id);
    }
}