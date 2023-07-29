package com.payment.service;

import com.payment.model.entity.Transaction;
import com.payment.model.response.TransactionListResponse;
import com.payment.model.response.TransactionResponse;
import com.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentTransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional(rollbackFor = Exception.class,
            isolation = Isolation.REPEATABLE_READ)
    public List<Transaction> removeTransactionsOlderThan(Instant instant) {
        return this.transactionRepository
                .deleteByCreatedAtBefore(instant)
                .filter(transactions -> !transactions.isEmpty())
                .orElse(List.of());
    }

    public TransactionListResponse getAllTransactionsOfMerchant(String merchantEmail) {
        return Optional.of(transactionRepository.findAllByCustomerEmailOrderByCreatedAtAsc(merchantEmail))
                .filter(txs -> !txs.isEmpty())
                .map(this::convertToListResponse)
                .orElse(TransactionListResponse.EMPTY);
    }

    public TransactionListResponse getMerchantTransactions() {
        return getAllTransactionsOfMerchant(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
    }

    private TransactionListResponse convertToListResponse(List<Transaction> transactions) {
        return new TransactionListResponse(
                transactions.stream()
                        .map(tx -> new TransactionResponse(tx.getAmount(),
                                tx.getTransactionStatus(),
                                tx.getCustomerPhone(),
                                tx.getCustomerEmail(),
                                tx.getId().toString(),
                                tx.getReferenceId()
                        ))
                        .toList());
    }
}
