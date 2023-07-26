package com.payment.configuration;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        final var errorMap = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.badRequest()
                .body(new ValidationErrorResponse(errorMap));
    }

    private record ValidationErrorResponse(Map<String, String> errors) {
    }


}
