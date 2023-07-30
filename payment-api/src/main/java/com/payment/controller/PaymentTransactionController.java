package com.payment.controller;

import com.payment.model.GenericErrorResponse;
import com.payment.model.request.AuthorizeTransactionRequest;
import com.payment.model.request.ChargedTransactionRequest;
import com.payment.model.request.RefundTransactionRequest;
import com.payment.model.request.ReverseTransactionRequest;
import com.payment.service.PaymentTransactionService;
import com.payment.validator.ChargeTransactionRequestValidator;
import com.payment.validator.RefundTransactionRequestValidator;
import com.payment.validator.ReverseTransactionRequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;
    private final ReverseTransactionRequestValidator reverseTransactionRequestValidator;
    private final RefundTransactionRequestValidator refundTransactionRequestValidator;
    private final ChargeTransactionRequestValidator chargeTransactionRequestValidator;

    @InitBinder("reverseTransactionRequest")
    public void reverseTransactionInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(reverseTransactionRequestValidator);
    }

    @InitBinder("refundTransactionRequest")
    public void refundTransactionInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(refundTransactionRequestValidator);
    }

    @InitBinder("chargedTransactionRequest")
    public void chargeTransactionInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(chargeTransactionRequestValidator);
    }

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

    @PostMapping("/refund")
    private ResponseEntity<?> refundTransaction(@RequestBody @Valid
                                                RefundTransactionRequest refundTransactionRequest) {

        final var transactionResponse = paymentTransactionService.refundTransaction(refundTransactionRequest);
        return transactionResponse != null ?
                ResponseEntity.ok(transactionResponse) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Failed to refund transaction."));
    }
}
