package com.payment.model.response;

import java.util.List;

public record TransactionListResponse(List<TransactionResponse> transactionResponseList) {
    public static final TransactionListResponse EMPTY =
            new TransactionListResponse(List.of());
}
