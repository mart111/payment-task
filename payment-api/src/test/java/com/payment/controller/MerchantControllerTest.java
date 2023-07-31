package com.payment.controller;

import com.payment.model.TransactionStatus;
import com.payment.model.response.TransactionListResponse;
import com.payment.model.response.TransactionResponse;
import com.payment.service.PaymentTransactionService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerchantControllerTest {

    @Mock
    PaymentTransactionService paymentTransactionService;

    @InjectMocks
    MerchantController testSubject;

    @Test
    void getAllTransactions() {

        final var transactionListResponse = new TransactionListResponse(
                List.of(
                        new TransactionResponse(BigDecimal.TEN,
                                TransactionStatus.APPROVED,
                                StringUtils.EMPTY,
                                StringUtils.EMPTY,
                                StringUtils.EMPTY,
                                StringUtils.EMPTY,
                                1L)
                )
        );

        when(paymentTransactionService.getMerchantTransactions())
                .thenReturn(transactionListResponse);

        final var allTransactions = testSubject.getAllTransactions();

        assertAll(() -> assertEquals(1, allTransactions.getBody()
                        .transactionResponseList()
                        .size()),
                () -> assertEquals(transactionListResponse, allTransactions.getBody()),
                () -> assertEquals(HttpStatus.OK, allTransactions.getStatusCode()));

        verify(paymentTransactionService)
                .getMerchantTransactions();
    }

    @Test
    void getAllTransactions_ListIsEmpty() {

        when(paymentTransactionService.getMerchantTransactions())
                .thenReturn(TransactionListResponse.EMPTY);

        final var allTransactions = testSubject.getAllTransactions();

        assertAll(() -> assertEquals(0, allTransactions.getBody()
                        .transactionResponseList()
                        .size()),
                () -> assertEquals(TransactionListResponse.EMPTY, allTransactions.getBody()),
                () -> assertEquals(HttpStatus.OK, allTransactions.getStatusCode()));

        verify(paymentTransactionService)
                .getMerchantTransactions();
    }

    @Test
    void getAllTransactions_ServiceThrows() {

        when(paymentTransactionService.getMerchantTransactions())
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () ->
                testSubject.getAllTransactions());

        verify(paymentTransactionService)
                .getMerchantTransactions();
    }
}