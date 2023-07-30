package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ChargedTransactionRequest {

    @JsonProperty("merchant_id")
    @NotNull(message = "Merchant id is required.")
    @Setter(AccessLevel.NONE)
    protected Long merchantId;

    @JsonProperty("authorized_transaction_id")
    @NotNull(message = "Authorized transaction Id should not be null.")
    @Setter(AccessLevel.NONE)
    private String authorizedTransactionId;
}
