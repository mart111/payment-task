package com.payment.model.request;

import com.opencsv.bean.CsvBindByName;
import com.payment.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRegistrationRequest {

    @Email(message = "Email address is not valid.")
    @NotNull(message = "This field is required.")
    @CsvBindByName(column = "email", required = true)
    private String username;

    @NotNull(message = "This field is required.")
    @Size(min = 6, message = "Password should contain at least 6 characters.")
    @CsvBindByName(required = true)
    private String password;

    @NotNull(message = "This field is required.")
    @CsvBindByName(required = true)
    private Role role;

    @NotNull(message = "This field is required.")
    @CsvBindByName(required = true)
    private String name;

    @CsvBindByName
    private String description;
}
