package com.payment.configuration;

import com.payment.exception.MerchantNotEligibleForRemovalException;
import com.payment.model.GenericErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MerchantRemovalExceptionHandler {


    @ExceptionHandler(MerchantNotEligibleForRemovalException.class)
    public ResponseEntity<GenericErrorResponse> handle() {
        return ResponseEntity.badRequest()
                .body(GenericErrorResponse.withError("Merchant can't be deleted, due to related payment history."));
    }
}
