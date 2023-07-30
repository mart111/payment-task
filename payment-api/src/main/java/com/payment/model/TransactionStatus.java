package com.payment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransactionStatus {

    AUTHORIZED("authorized"),
    APPROVED("approved"),
    REVERSED("reversed"),
    REFUNDED("refunded");

    @Getter
    private final String statusName;
}
