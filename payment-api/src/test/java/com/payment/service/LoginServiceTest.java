package com.payment.service;

import com.payment.model.AuthenticationToken;
import com.payment.model.Role;
import com.payment.model.entity.User;
import com.payment.model.request.LoginRequest;
import com.payment.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtService jwtService;

    @InjectMocks
    LoginService testSubject;

    @Test
    void login() {

        User user = new User();
        user.setEmail("test@gmail.ru");
        user.setPassword("test");
        user.setRole(Role.MERCHANT);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(UsernamePasswordAuthenticationToken.authenticated(
                        user, user.getPassword(),
                        user.getAuthorities()
                ));
        when(jwtService.generateToken(any(User.class)))
                .thenReturn(new AuthenticationToken("token", new Date()));

        final var loginResponse = testSubject.login(new LoginRequest("test@gmail.ru", "test"));
        assertAll(() -> assertNotNull(loginResponse),
                () -> assertNotNull(loginResponse.getAuthenticationToken()));
        verify(jwtService)
                .generateToken(any(User.class));
        verify(authenticationManager)
                .authenticate(any(Authentication.class));
    }

    @Test
    void login_WhenAuthenticationFails() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () ->
                testSubject.login(new LoginRequest("test@gmail.ru", "test")));

        verify(authenticationManager)
                .authenticate(any(Authentication.class));
        verify(jwtService, never())
                .generateToken(any(User.class));

    }
}