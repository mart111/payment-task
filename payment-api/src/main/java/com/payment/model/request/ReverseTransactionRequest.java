package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReverseTransactionRequest {

    @JsonProperty("authorized_transaction_id")
    @NotNull(message = "Authorized transaction Id should not be null.")
    @Setter(AccessLevel.NONE)
    private String authorizedTransactionId;
}
