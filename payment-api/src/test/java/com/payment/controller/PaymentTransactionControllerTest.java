package com.payment.controller;

import com.payment.model.GenericErrorResponse;
import com.payment.model.TransactionStatus;
import com.payment.model.request.AuthorizeTransactionRequest;
import com.payment.model.request.ChargedTransactionRequest;
import com.payment.model.request.RefundTransactionRequest;
import com.payment.model.request.ReverseTransactionRequest;
import com.payment.model.response.TransactionResponse;
import com.payment.service.PaymentTransactionService;
import com.payment.validator.ChargeTransactionRequestValidator;
import com.payment.validator.RefundTransactionRequestValidator;
import com.payment.validator.ReverseTransactionRequestValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionControllerTest {

    private static final TransactionResponse AUTHORIZE_TRANSACTION_RESPONSE =
            new TransactionResponse(BigDecimal.TEN,
                    TransactionStatus.AUTHORIZED,
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    null);

    private static final TransactionResponse CHARGED_TRANSACTION_RESPONSE =
            new TransactionResponse(BigDecimal.TEN,
                    TransactionStatus.APPROVED,
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    1L);

    private static final TransactionResponse REVERSED_TRANSACTION_RESPONSE =
            new TransactionResponse(BigDecimal.TEN,
                    TransactionStatus.REVERSED,
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    1L);

    private static final TransactionResponse REFUND_TRANSACTION_REQUEST =
            new TransactionResponse(BigDecimal.TEN,
                    TransactionStatus.REFUNDED,
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    1L);

    @Mock
    PaymentTransactionService paymentTransactionService;

    @Mock
    ReverseTransactionRequestValidator reverseTransactionRequestValidator;

    @Mock
    RefundTransactionRequestValidator refundTransactionRequestValidator;

    @Mock
    ChargeTransactionRequestValidator chargeTransactionRequestValidator;

    @InjectMocks
    PaymentTransactionController testSubject;

    @Test
    void submitTransaction() {

        when(paymentTransactionService.authorizeTransaction(
                any(AuthorizeTransactionRequest.class)
        )).thenReturn(AUTHORIZE_TRANSACTION_RESPONSE);

        final ResponseEntity<?> responseEntity = testSubject.submitTransaction(
                new AuthorizeTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(AUTHORIZE_TRANSACTION_RESPONSE, responseEntity.getBody())
        );

        verify(paymentTransactionService)
                .authorizeTransaction(any(AuthorizeTransactionRequest.class));
    }

    @Test
    void submitTransaction_serviceReturnsNull() {

        when(paymentTransactionService.authorizeTransaction(
                any(AuthorizeTransactionRequest.class)
        )).thenReturn(null);

        final ResponseEntity<?> responseEntity = testSubject.submitTransaction(
                new AuthorizeTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof GenericErrorResponse)
        );

        verify(paymentTransactionService)
                .authorizeTransaction(any(AuthorizeTransactionRequest.class));
    }

    @Test
    void chargeTransaction() {
        when(paymentTransactionService.chargeTransaction(
                any(ChargedTransactionRequest.class)
        )).thenReturn(CHARGED_TRANSACTION_RESPONSE);

        final ResponseEntity<?> responseEntity = testSubject.chargeTransaction(
                new ChargedTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(CHARGED_TRANSACTION_RESPONSE, responseEntity.getBody())
        );

        verify(paymentTransactionService)
                .chargeTransaction(any(ChargedTransactionRequest.class));
    }

    @Test
    void chargeTransaction_serviceReturnsNull() {
        when(paymentTransactionService.chargeTransaction(
                any(ChargedTransactionRequest.class)
        )).thenReturn(null);

        final ResponseEntity<?> responseEntity = testSubject.chargeTransaction(
                new ChargedTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof GenericErrorResponse)
        );

        verify(paymentTransactionService)
                .chargeTransaction(any(ChargedTransactionRequest.class));
    }

    @Test
    void refundTransaction() {
        when(paymentTransactionService.refundTransaction(
                any(RefundTransactionRequest.class)
        )).thenReturn(REFUND_TRANSACTION_REQUEST);

        final ResponseEntity<?> responseEntity = testSubject.refundTransaction(
                new RefundTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(REFUND_TRANSACTION_REQUEST, responseEntity.getBody())
        );

        verify(paymentTransactionService)
                .refundTransaction(any(RefundTransactionRequest.class));
    }

    @Test
    void refundTransaction_serviceReturnsNull() {
        when(paymentTransactionService.refundTransaction(
                any(RefundTransactionRequest.class)
        )).thenReturn(null);

        final ResponseEntity<?> responseEntity = testSubject.refundTransaction(
                new RefundTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof GenericErrorResponse)
        );

        verify(paymentTransactionService)
                .refundTransaction(any(RefundTransactionRequest.class));
    }

    @Test
    void reverseTransaction() {
        when(paymentTransactionService.reverseTransaction(
                any(ReverseTransactionRequest.class)
        )).thenReturn(REVERSED_TRANSACTION_RESPONSE);

        final ResponseEntity<?> responseEntity = testSubject.reverseTransaction(
                new ReverseTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(REVERSED_TRANSACTION_RESPONSE, responseEntity.getBody())
        );

        verify(paymentTransactionService)
                .reverseTransaction(any(ReverseTransactionRequest.class));
    }

    @Test
    void reverseTransaction_serviceReturnsNull() {
        when(paymentTransactionService.reverseTransaction(
                any(ReverseTransactionRequest.class)
        )).thenReturn(null);

        final ResponseEntity<?> responseEntity = testSubject.reverseTransaction(
                new ReverseTransactionRequest()
        );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof GenericErrorResponse)
        );

        verify(paymentTransactionService)
                .reverseTransaction(any(ReverseTransactionRequest.class));
    }
}