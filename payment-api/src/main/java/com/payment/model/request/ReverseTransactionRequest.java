package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReverseTransactionRequest {

    @JsonProperty("authorized_transaction_id")
    @NotNull(message = "Authorized transaction Id should not be null.")
    private String authorizedTransactionId;
}
