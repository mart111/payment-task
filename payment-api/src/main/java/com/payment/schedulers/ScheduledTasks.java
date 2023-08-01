package com.payment.schedulers;

import com.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class ScheduledTasks {

    private final PaymentTransactionService paymentTransactionService;

    @Scheduled(cron = "${app.scheduler.cron}")
    @Async
    public void removeTransactionsOlderThanOneHour() {
        log.debug("Job: 'removeTransactionsOlderThanOneHour' started.");
        log.debug("Running...");
        final var removeTransactions = paymentTransactionService.removeTransactionsOlderThan(Instant.now()
                .minus(1, ChronoUnit.HOURS));
        log.debug("Job: 'removeTransactionsOlderThanOneHour' finished execution.");
        log.debug("Removed items count: {}", removeTransactions.size());
    }
}
