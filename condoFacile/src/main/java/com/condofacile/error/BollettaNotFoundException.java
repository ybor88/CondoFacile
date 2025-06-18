package com.condofacile.error;

public class BollettaNotFoundException extends RuntimeException {
    public BollettaNotFoundException(Integer id) {
        super("Bolletta non trovata con ID: " + id);
    }
}