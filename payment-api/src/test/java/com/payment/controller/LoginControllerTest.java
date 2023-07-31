package com.payment.controller;

import com.payment.model.request.LoginRequest;
import com.payment.model.response.LoginResponse;
import com.payment.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    LoginService loginService;

    @InjectMocks
    LoginController testSubject;

    @Test
    void login() {
        final var loginResponse = LoginResponse.builder()
                .name("test")
                .username("test@gmail.com")
                .build();
        when(loginService.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);
        final var response = testSubject.login(new LoginRequest("test", "test"));
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(loginResponse, response.getBody())
        );

        verify(loginService)
                .login(any());
    }

    @Test
    void login_whenFailed() {
        when(loginService.login(any(LoginRequest.class)))
                .thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () ->
                testSubject.login(new LoginRequest("test", "test"))
        );

        verify(loginService)
                .login(any());
    }
}