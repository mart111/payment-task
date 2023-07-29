package com.payment.model.response;

import java.math.BigDecimal;

public record MerchantResponse(
        String name,
        String email,
        BigDecimal transactionTotalSum,
        String description
) {
}
