package com.payment.model.request;

import com.payment.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantEditRequest(
        @NotNull(message = "This field is required.")
        String name,
        String description,

        @Email(message = "Email address is not valid.")
        @NotNull(message = "This field is required.")
        String username,

        @NotNull(message = "This field is required.")
        @Size(min = 6, message = "Password should contain at least 6 characters.")
        String password,
        Status status) {

}
