package com.payment.controller;

import com.payment.model.GenericErrorResponse;
import com.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentTransactionService paymentTransactionService;

    @GetMapping("transactions")
    public ResponseEntity<?> getAllTransactions(@RequestParam String merchantEmail) {
        return hasText(merchantEmail) ?
                ResponseEntity.ok(paymentTransactionService.getAllTransactions(merchantEmail)) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Merchant email should be provided"));


    }
}
