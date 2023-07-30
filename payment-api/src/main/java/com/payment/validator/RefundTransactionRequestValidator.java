package com.payment.validator;

import com.payment.model.TransactionStatus;
import com.payment.model.request.RefundTransactionRequest;
import com.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefundTransactionRequestValidator implements Validator {

    private final TransactionRepository transactionRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RefundTransactionRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final var refundTransactionRequest = (RefundTransactionRequest) target;
        final var chargedTransactionId = refundTransactionRequest.getChargedTransactionId();
        final var chargedTransaction = transactionRepository.findById(UUID.fromString(chargedTransactionId))
                .orElse(null);
        if (chargedTransaction == null) {
            errors.rejectValue("chargedTransactionId", "Couldn't find 'CHARGED' transaction to refund.");
            return;
        }

        if (chargedTransaction.getTransactionStatus() != TransactionStatus.APPROVED) {
            errors.rejectValue("chargedTransactionId", String.format(
                    "Couldn't refund Transaction with status '%s'", chargedTransaction.getTransactionStatus()
            ));
        }
    }
}
