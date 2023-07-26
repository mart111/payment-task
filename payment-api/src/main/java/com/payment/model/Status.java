package com.payment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {

    ACTIVE("active"),
    INACTIVE("inactive");

    private final String statusName;
}
