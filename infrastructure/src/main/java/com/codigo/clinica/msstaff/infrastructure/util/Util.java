package com.codigo.clinica.msstaff.infrastructure.util;

import com.codigo.clinica.msstaff.infrastructure.exceptions.JsonConversionException;
import com.codigo.clinica.msstaff.infrastructure.exceptions.ResponseValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Util {
    private Util() {
    }

    public static <T> String convertToString(T objectTo){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(objectTo);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException("Fail convert object to Json string.",e);
        }
    }

    public static <T> T convertFromString(String json, Class<T> classType){
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            return objectMapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException("Fail to convert Json string to object.",e);
        }
    }

    public static  <T> T validateResponse(ResponseEntity<T> reponse){
        if(reponse.getStatusCode() == HttpStatus.OK){
            return reponse.getBody();
        }else if (reponse.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new ResponseValidationException("Registro no encontrado.");
        }else{
            throw new ResponseValidationException("Un error desconocido ha sucedido.");
        }
    }
}
