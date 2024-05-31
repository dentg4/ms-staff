package com.codigo.clinica.msstaff.application.exception;

import com.codigo.clinica.msstaff.infrastructure.exceptions.JsonConversionException;
import com.codigo.clinica.msstaff.infrastructure.exceptions.ResponseValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptions {

    private static final String MSJ_RESPONSE= "Proceso fallido, volver a intertarlo después.";

    @ExceptionHandler(JsonConversionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> jsonConversionException(final JsonConversionException e,final WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(e.getMessage())
                .error(MSJ_RESPONSE).build(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResponseValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> responseStatusException(final ResponseValidationException e, final WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(e.getMessage())
                .error(MSJ_RESPONSE).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exception(final Exception e, final WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(e.getMessage())
                .error(MSJ_RESPONSE).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
