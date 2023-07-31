package com.payment.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class MerchantRemovalExceptionHandlerTest {

    MerchantRemovalExceptionHandler testSubject =
            new MerchantRemovalExceptionHandler();

    @Test
    void handle() {
        final var response = testSubject.handle();
        assertAll(() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()));
    }
}