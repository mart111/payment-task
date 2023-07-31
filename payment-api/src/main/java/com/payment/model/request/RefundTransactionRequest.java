package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefundTransactionRequest {

    @JsonProperty("charged_transaction_id")
    @NotNull(message = "Charged transaction Id should not be null.")
    @Setter(AccessLevel.NONE)
    private String chargedTransactionId;
}
