package com.condofacile.error;

public class BollettaPdfNotCreatedException extends RuntimeException {
    public BollettaPdfNotCreatedException(String message) {
        super(message);
    }
}
