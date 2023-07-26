package com.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    @JsonProperty("admin")
    ADMIN("admin"),

    @JsonProperty("merchant")
    MERCHANT("merchant");

    private final String roleName;
}
