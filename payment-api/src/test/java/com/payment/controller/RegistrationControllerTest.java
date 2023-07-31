package com.payment.controller;

import com.payment.model.Role;
import com.payment.model.request.UserRegistrationRequest;
import com.payment.model.response.UserRegistrationResponse;
import com.payment.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    RegistrationService registrationService;

    @InjectMocks
    RegistrationController testSubject;

    @Test
    void registerUser() {
        final var registrationResponse = new UserRegistrationResponse("test", "test",
                "test@gmail.com",
                Role.ADMIN);
        when(registrationService.register(any(UserRegistrationRequest.class)))
                .thenReturn(registrationResponse);

        final var response = testSubject.registerUser(new UserRegistrationRequest());
        assertAll(() -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(registrationResponse, response.getBody()));
        verify(registrationService)
                .register(any(UserRegistrationRequest.class));
    }

    @Test
    void registerUser_throwsValidationException() {
        when(registrationService.register(any(UserRegistrationRequest.class)))
                .thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class,
                () -> testSubject.registerUser(new UserRegistrationRequest()));
        verify(registrationService)
                .register(any(UserRegistrationRequest.class));
    }
}