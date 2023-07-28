package com.payment.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "withError")
public class GenericErrorResponse {
    private final String errorMessage;
}
