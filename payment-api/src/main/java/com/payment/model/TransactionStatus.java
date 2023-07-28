package com.payment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransactionStatus {

    AUTHORIZED("authorized"),
    APPROVED("approved"),
    REVERSED("charged"),
    REFUNDED("refunded"),
    ERROR("error");

    @Getter
    private final String statusName;
}
