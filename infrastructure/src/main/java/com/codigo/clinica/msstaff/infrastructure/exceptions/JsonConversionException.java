package com.codigo.clinica.msstaff.infrastructure.exceptions;

public class JsonConversionException extends RuntimeException{
    public JsonConversionException(String msj, Throwable cause){
        super(msj, cause);
    }
}
