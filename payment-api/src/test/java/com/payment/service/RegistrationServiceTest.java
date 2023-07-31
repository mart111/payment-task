package com.payment.service;

import com.payment.exception.DuplicateUsernameException;
import com.payment.model.Role;
import com.payment.model.entity.User;
import com.payment.model.request.UserRegistrationRequest;
import com.payment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    UserRegistrationRequest request = new UserRegistrationRequest();

    User user = new User();

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    MerchantService merchantService;

    @InjectMocks
    RegistrationService registrationService;

    @BeforeEach
    void setup() {
        request.setName("Test");
        request.setPassword("Test123");
        request.setRole(Role.ADMIN);
        request.setUsername("Test@mail.ru");

        user.setId(1L);
        user.setName("Test");
        user.setPassword("Test123");
        user.setRole(Role.ADMIN);
        user.setEmail("Test@mail.ru");


    }

    @Test
    void register() {
        when(merchantService.usernameAlreadyExists(anyString()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        final var response = registrationService.register(request);
        assertEquals(1L, user.getId());
        verify(userRepository)
                .save(any());
        verify(merchantService)
                .usernameAlreadyExists(anyString());
    }

    @Test
    void register_ShouldThrowWhenUsernameAlreadyExists() {
        when(merchantService.usernameAlreadyExists(anyString()))
                .thenReturn(true);

        assertThrows(DuplicateUsernameException.class,
                () -> registrationService.register(request));

        assertEquals(1L, user.getId());
        verify(userRepository, never())
                .save(any());
        verify(merchantService)
                .usernameAlreadyExists(anyString());
    }

    @Test
    void registerAll() {
        when(merchantService.usernameAlreadyExists(anyString()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        final var response = registrationService.registerAll(List.of(
                request, request
        ));

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(1L, user.getId());
        verify(userRepository, times(2))
                .save(any());
        verify(merchantService, times(2))
                .usernameAlreadyExists(anyString());
    }

    @Test
    void registerAll_DuplicateUsernameShouldThrow() {
        when(merchantService.usernameAlreadyExists(anyString()))
                .thenReturn(false, true);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        assertThrows(DuplicateUsernameException.class,
                () -> registrationService.registerAll(List.of(request, request)));

        verify(userRepository)
                .save(any());
        verify(merchantService, times(2))
                .usernameAlreadyExists(anyString());
    }
}