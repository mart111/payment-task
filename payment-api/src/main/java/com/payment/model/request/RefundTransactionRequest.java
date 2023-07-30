package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundTransactionRequest {

    @JsonProperty("charged_transaction_id")
    @NotNull(message = "Charged transaction Id should not be null.")
    private String chargedTransactionId;
}
