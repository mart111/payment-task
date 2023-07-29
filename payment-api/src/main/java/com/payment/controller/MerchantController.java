package com.payment.controller;

import com.payment.model.response.TransactionListResponse;
import com.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final PaymentTransactionService paymentTransactionService;

    @GetMapping("transactions")
    public ResponseEntity<TransactionListResponse> getAllTransactions() {
        return ResponseEntity.ok(paymentTransactionService.getMerchantTransactions());
    }
}
