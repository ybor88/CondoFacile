package com.condofacile.error;

public class AvvisoNotFoundException extends RuntimeException {

    public AvvisoNotFoundException(Integer id) {
        super("Avviso con ID " + id + " non trovato.");
    }

    public AvvisoNotFoundException(String message) {
        super(message);
    }
}