package com.codigo.clinica.msstaff.infrastructure.exceptions;

public class ResponseValidationException extends RuntimeException {
    public ResponseValidationException(String message) {
        super(message);
    }
}