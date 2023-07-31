package com.payment.configuration;

import com.payment.exception.DuplicateUsernameException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateUserNameExceptionHandlerTest {

    DuplicateUserNameExceptionHandler testSubject
            = new DuplicateUserNameExceptionHandler();

    @Test
    void duplicateUsernameHandler() {
        String msg = "User email already exists";
        final var ex = new DuplicateUsernameException(msg);

        final var response = testSubject.duplicateUsernameHandler(ex);
        assertAll(() -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(response.getBody().containsKey("username")));
    }
}