package com.payment.controller;

import com.payment.model.request.UserRegistrationRequest;
import com.payment.model.response.UserRegistrationResponse;
import com.payment.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Handles the registration request of user types - merchant and admin.
     *
     * @param registrationRequest - the JSON representation of {@link UserRegistrationRequest}
     * @return {@link ResponseEntity<UserRegistrationResponse>} with status code <code>201</code> if request succeeded,
     */
    @PostMapping
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody @Valid
                                          UserRegistrationRequest registrationRequest) {
        final var registrationResponse = registrationService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationResponse);
    }
}
