package com.payment.model.response;

import com.payment.model.Role;

public record UserRegistrationResponse(
        String name,
        String description,
        String email,
        Role role) {
}
