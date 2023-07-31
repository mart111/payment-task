package com.payment.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationExceptionHandlerTest {

    ValidationExceptionHandler testSubject =
            new ValidationExceptionHandler();

    @Mock
    MethodArgumentNotValidException methodArgumentNotValidException;

    @Test
    void handleValidationErrors() {
        BindingResult bindingResult =
                new BindException(new Object(), "objectName");

        bindingResult.addError(new ObjectError("username", "not a valid username"));
        bindingResult.addError(new ObjectError("password", "Password should contain at least 6 characters."));

        when(methodArgumentNotValidException.getBindingResult())
                .thenReturn(bindingResult);

        final var errorResponseEntity = testSubject.handleValidationErrors(methodArgumentNotValidException);
        assertAll(() -> assertEquals(HttpStatus.BAD_REQUEST, errorResponseEntity.getStatusCode()),
                () -> assertNotNull(errorResponseEntity.getBody()),
                () -> assertTrue(errorResponseEntity.getBody()
                        .getErrors()
                        .containsKey("username")),
                () -> assertTrue(errorResponseEntity.getBody()
                        .getErrors()
                        .containsKey("password")));
    }
}