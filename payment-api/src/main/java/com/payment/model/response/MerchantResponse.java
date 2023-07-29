package com.payment.model.response;

import com.payment.model.Status;

import java.math.BigDecimal;

public record MerchantResponse(
        long id,
        String name,
        String email,
        Status status,
        BigDecimal transactionTotalSum,
        String description) {
}
