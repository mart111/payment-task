package com.payment.configuration;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class CsvRequiredHeaderValuesMissingExceptionHandlerTest {

    CsvRequiredHeaderValuesMissingExceptionHandler testSubject
            = new CsvRequiredHeaderValuesMissingExceptionHandler();

    @Test
    void handle() {

        String msg = "Mandatory field is missing";
        final var fieldEmptyException = new CsvRequiredFieldEmptyException(msg);

        final var response = testSubject.handle(fieldEmptyException);
        assertAll(() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(response.getBody().getErrorMessage().contains(msg)));
    }
}