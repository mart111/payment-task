package com.payment.configuration;

import com.payment.exception.DuplicateUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class DuplicateUserNameExceptionHandler {


    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, String>> duplicateUsernameHandler(DuplicateUsernameException e) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(Map.of("username", e.getMessage()));
    }
}
