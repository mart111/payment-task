package com.payment.controller;

import com.payment.model.request.LoginRequest;
import com.payment.model.response.LoginResponse;
import com.payment.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid
                                               LoginRequest loginRequest) {
        final var loginResponse = loginService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
