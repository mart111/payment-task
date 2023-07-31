package com.payment.schedulers;

import com.payment.service.PaymentTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledTasksTest {

    @Mock
    PaymentTransactionService paymentTransactionService;

    @InjectMocks
    ScheduledTasks testSubject;

    @Test
    void removeTransactionsOlderThanOneHour() {
        when(paymentTransactionService.removeTransactionsOlderThan(any(Instant.class)))
                .thenReturn(List.of());

        testSubject.removeTransactionsOlderThanOneHour();

        verify(paymentTransactionService)
                .removeTransactionsOlderThan(any(Instant.class));
    }
}