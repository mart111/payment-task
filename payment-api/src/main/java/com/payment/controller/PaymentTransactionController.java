package com.payment.controller;

import com.payment.model.GenericErrorResponse;
import com.payment.model.request.AuthorizeTransactionRequest;
import com.payment.model.request.ChargedTransactionRequest;
import com.payment.model.request.ReverseTransactionRequest;
import com.payment.service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @PostMapping("/authorize")
    private ResponseEntity<?> submitTransaction(@RequestBody @Valid
                                                AuthorizeTransactionRequest authorizeTransactionRequest) {

        final var transactionResponse = paymentTransactionService.authorizeTransaction(authorizeTransactionRequest);
        return transactionResponse != null ?
                ResponseEntity.ok(transactionResponse) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Failed to authorize transaction."));
    }

    @PostMapping("/reverse")
    private ResponseEntity<?> reverseTransaction(@RequestBody @Valid ReverseTransactionRequest reverseTransactionRequest) {
        final var transactionResponse = paymentTransactionService.reverseTransaction(reverseTransactionRequest);
        return transactionResponse != null ?
                ResponseEntity.ok(transactionResponse) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Failed to reverse transaction."));
    }

    @PostMapping("/charge")
    private ResponseEntity<?> chargeTransaction(@RequestBody @Valid
                                                ChargedTransactionRequest chargedTransactionRequest) {

        final var transactionResponse = paymentTransactionService.chargeTransaction(chargedTransactionRequest);
        return transactionResponse != null ?
                ResponseEntity.ok(transactionResponse) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Failed to charge transaction."));
    }

}
