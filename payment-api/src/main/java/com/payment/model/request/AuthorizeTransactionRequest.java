package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class AuthorizeTransactionRequest {

    @NotNull(message = "This field is required.")
    @Email(message = "Not a valid e-mail address.")
    @JsonProperty("customer_email")
    @Setter(AccessLevel.NONE)
    private String customerEmail;

    @NotNull(message = "Customer phone should not be null.")
    @Pattern(regexp = "^\\d{10}+$")
    @JsonProperty("customer_phone")
    @Setter(AccessLevel.NONE)
    private String customerPhone;

    @NotNull(message = "Amount should not be empty.")
    @Pattern(regexp = "\\d+(?:\\.\\d+)?|\\.\\d+", message = "Not a valid amount. E.g, 10, 10.00")
    @Setter(AccessLevel.NONE)
    private String amount;
}
