package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChargedTransactionRequest {

    @JsonProperty("merchant_id")
    @NotNull(message = "Merchant id is required.")
    protected Long merchantId;

    @JsonProperty("authorized_transaction_id")
    @NotNull(message = "Authorized transaction Id should not be null.")
    private String authorizedTransactionId;
}
