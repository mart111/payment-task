package com.payment.service;

import com.payment.model.entity.User;
import com.payment.model.request.LoginRequest;
import com.payment.model.response.LoginResponse;
import com.payment.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        final var principal = (User) authentication.getPrincipal();
        final var token = jwtService.generateToken(principal);
        return LoginResponse.builder()
                .username(principal.getUsername())
                .status(principal.getStatus())
                .description(principal.getDescription())
                .name(principal.getName())
                .role(principal.getRole())
                .authenticationToken(token)
                .build();

    }
}
