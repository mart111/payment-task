package com.payment.service;

import com.payment.model.entity.Transaction;
import com.payment.model.response.TransactionListResponse;
import com.payment.model.response.TransactionResponse;
import com.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentTransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionListResponse getAllTransactions(String merchantEmail) {
        return Optional.of(transactionRepository.findAllByCustomerEmailOrderByCreatedAtAsc(merchantEmail))
                .filter(txs -> !txs.isEmpty())
                .map(this::convertToListResponse)
                .orElse(TransactionListResponse.EMPTY);
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
