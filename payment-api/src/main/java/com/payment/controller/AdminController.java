package com.payment.controller;

import com.payment.model.GenericErrorResponse;
import com.payment.model.response.MerchantListResponse;
import com.payment.service.MerchantService;
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
public class AdminController {

    private final PaymentTransactionService paymentTransactionService;
    private final MerchantService merchantService;

    @GetMapping
    public ResponseEntity<MerchantListResponse> fetchAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }

    @GetMapping("transactions")
    public ResponseEntity<?> getAllTransactionsOfMerchant(@RequestParam String merchantEmail) {
        return hasText(merchantEmail) ?
                ResponseEntity.ok(paymentTransactionService.getAllTransactionsOfMerchant(merchantEmail)) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Merchant's email should be provided."));
    }
}
