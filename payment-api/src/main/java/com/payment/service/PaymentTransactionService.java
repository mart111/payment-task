package com.payment.service;

import com.payment.model.entity.AuthorizeTransaction;
import com.payment.model.entity.ChargeTransaction;
import com.payment.model.entity.Merchant;
import com.payment.model.entity.Transaction;
import com.payment.model.request.AuthorizeTransactionRequest;
import com.payment.model.request.ChargedTransactionRequest;
import com.payment.model.response.TransactionListResponse;
import com.payment.model.response.TransactionResponse;
import com.payment.repository.MerchantRepository;
import com.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentTransactionService {

    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;

    @Transactional(rollbackFor = Exception.class,
            isolation = Isolation.REPEATABLE_READ)
    public List<Transaction> removeTransactionsOlderThan(Instant instant) {
        return this.transactionRepository
                .deleteByCreatedAtBefore(instant)
                .filter(transactions -> !transactions.isEmpty())
                .orElse(List.of());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public TransactionResponse authorizeTransaction(AuthorizeTransactionRequest authorizeTransactionRequest) {
        return Optional.of(transactionRepository.save(convertToAuthorizeTransaction(authorizeTransactionRequest)))
                .map(this::convertToTransactionResponse)
                .orElse(null);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public TransactionResponse chargeTransaction(ChargedTransactionRequest chargedTransactionRequest) {
        final var chargedTransaction = convertToChargedTransaction(chargedTransactionRequest);
        if (chargedTransaction == null) {
            log.warn("Authorized transaction not found by id: '{}'", chargedTransactionRequest.getAuthorizedTransactionId());
            return null;
        }
        final var transaction = transactionRepository.save(chargedTransaction);

        final var updatedMerchant = merchantRepository.findById(chargedTransactionRequest.getMerchantId())
                .map(merchant -> {
                    merchant.addToTotalSum(transaction.getAmount());
                    merchant.addTransaction(transaction);
                    return merchant;
                })
                .orElse(null);
        if (updatedMerchant == null) {
            log.warn("Merchant not found by id: '{}'", chargedTransactionRequest.getMerchantId());
            return null;
        }

        return convertToTransactionResponse(transaction);
    }

    public TransactionListResponse findAllTransactions(String merchantEmail) {
        return merchantRepository.findMerchantByEmail(merchantEmail)
                .map(Merchant::getTransactions)
                .map(this::convertToListResponse)
                .orElse(TransactionListResponse.EMPTY);
    }

    public TransactionListResponse getMerchantTransactions() {
        return findAllTransactions(
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

    private TransactionResponse convertToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getAmount(),
                transaction.getTransactionStatus(),
                transaction.getCustomerPhone(),
                transaction.getCustomerEmail(),
                transaction.getId().toString(),
                transaction.getReferenceId()
        );
    }

    private Transaction convertToAuthorizeTransaction(AuthorizeTransactionRequest request) {
        final var authorizeTransaction = new AuthorizeTransaction();
        authorizeTransaction.setAmount(new BigDecimal(request.getAmount()));
        authorizeTransaction.setCustomerEmail(request.getCustomerEmail());
        authorizeTransaction.setCustomerPhone(request.getCustomerPhone());

        return authorizeTransaction;
    }

    private Transaction convertToChargedTransaction(ChargedTransactionRequest request) {
        return transactionRepository.findById(UUID.fromString(request.getAuthorizedTransactionId()))
                .map(ChargeTransaction::wrap)
                .orElse(null);
    }
}
