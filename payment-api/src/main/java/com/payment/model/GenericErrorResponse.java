package com.payment.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "withError")
@Data
public class GenericErrorResponse {
    private final String errorMessage;
}
