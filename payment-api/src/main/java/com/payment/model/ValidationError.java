package com.payment.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
public class ValidationError {
    private HttpStatus status;
    private String error;
    private int count;
    private Map<String, Object> errors;
}
