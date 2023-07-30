package com.payment.service;

import com.payment.model.TransactionStatus;
import com.payment.model.entity.*;
import com.payment.model.request.AuthorizeTransactionRequest;
import com.payment.model.request.ChargedTransactionRequest;
import com.payment.model.request.RefundTransactionRequest;
import com.payment.model.request.ReverseTransactionRequest;
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

    public Transaction findTransactionById(UUID uuid) {
        return transactionRepository.getReferenceById(uuid);
    }

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
    public TransactionResponse reverseTransaction(ReverseTransactionRequest reverseTransactionRequest) {
        final var transaction = findTransactionById(UUID.fromString(reverseTransactionRequest
                .getAuthorizedTransactionId()));
        final var reversedTransaction = convertToReversedTransaction(reverseTransactionRequest);
        reversedTransaction.setReferenceId(transaction.getReferenceId());
        final var revTransaction = transactionRepository.save(reversedTransaction);
        transaction.setTransactionStatus(TransactionStatus.REVERSED);
        return convertToTransactionResponse(revTransaction);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public TransactionResponse chargeTransaction(ChargedTransactionRequest chargedTransactionRequest) {
        final var chargedTransaction = convertToChargedTransaction(chargedTransactionRequest);
        chargedTransaction.setReferenceId(chargedTransactionRequest.getAuthorizedTransactionId());
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
        transaction.setMerchantId(updatedMerchant.getId());
        return convertToTransactionResponse(transaction);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public TransactionResponse refundTransaction(RefundTransactionRequest refundTransactionRequest) {
        final var chargedTransaction =
                findTransactionById(UUID.fromString(refundTransactionRequest.getChargedTransactionId()));
        final var merchant = merchantRepository.findById(chargedTransaction.getMerchantId())
                .orElse(null);
        if (merchant == null) {
            return null;
        }

        final var transaction = convertToRefundedTransaction(refundTransactionRequest);
        transaction.setReferenceId(refundTransactionRequest.getChargedTransactionId());
        final var refundTransaction = transactionRepository.save(transaction);
        chargedTransaction.setTransactionStatus(TransactionStatus.REFUNDED);
        merchant.subtractFromTotalSum(refundTransaction.getAmount());
        merchant.addTransaction(refundTransaction);
        return convertToTransactionResponse(refundTransaction);
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
                                tx.getReferenceId(),
                                tx.getMerchantId()
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
                transaction.getReferenceId(),
                transaction.getMerchantId()
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
        return Optional.of(findTransactionById(UUID.fromString(request.getAuthorizedTransactionId())))
                .map(ChargeTransaction::wrap)
                .orElse(null);
    }

    private Transaction convertToReversedTransaction(ReverseTransactionRequest request) {
        return Optional.of(findTransactionById(UUID.fromString(request.getAuthorizedTransactionId())))
                .map(ReverseTransaction::wrap)
                .orElse(null);
    }

    private Transaction convertToRefundedTransaction(RefundTransactionRequest request) {
        return Optional.of(findTransactionById(UUID.fromString(request.getChargedTransactionId())))
                .map(RefundTransaction::wrap)
                .orElse(null);
    }
}
