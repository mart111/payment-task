package com.payment.model.request;

import com.payment.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MerchantEditRequest(
        String name,
        String description,

        @Email(message = "Email address is not valid.")
        String username,

        @Size(min = 6, message = "Password should contain at least 6 characters.")
        String password,
        Status status) {

}
