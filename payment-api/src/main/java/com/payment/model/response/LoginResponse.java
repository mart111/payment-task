package com.payment.model.response;

import com.payment.model.AuthenticationToken;
import com.payment.model.Role;
import com.payment.model.Status;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class LoginResponse {
    private String username;
    private String description;
    private String name;
    private Status status;
    private Role role;
    private AuthenticationToken authenticationToken;
}


