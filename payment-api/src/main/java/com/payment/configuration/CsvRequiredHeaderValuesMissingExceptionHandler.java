package com.payment.configuration;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.payment.model.GenericErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CsvRequiredHeaderValuesMissingExceptionHandler {

    @ExceptionHandler(CsvRequiredFieldEmptyException.class)
    public ResponseEntity<GenericErrorResponse> handle(CsvRequiredFieldEmptyException ex) {
        return ResponseEntity.badRequest()
                .body(GenericErrorResponse.withError(
                        String.format("CSV hs validation errors. %s Required header values are: '%s'",
                                ex.getMessage(),
                                "name, email, password, role")
                ));
    }
}
