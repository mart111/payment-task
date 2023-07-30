package com.payment.validator;

import com.payment.model.TransactionStatus;
import com.payment.model.request.ChargedTransactionRequest;
import com.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChargeTransactionRequestValidator implements Validator {

    private final TransactionRepository transactionRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChargedTransactionRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final var chargedTransactionRequest = (ChargedTransactionRequest) target;
        final var transaction = transactionRepository.findById(UUID.fromString(chargedTransactionRequest.
                        getAuthorizedTransactionId()))
                .orElse(null);
        if (transaction == null) {
            errors.rejectValue("authorizedTransactionId", "Authorized transaction not found.");
            return;
        }

        if (transaction.getTransactionStatus() != TransactionStatus.AUTHORIZED) {
            errors.rejectValue("authorizedTransactionId",
                    "Couldn't accept charge transaction. Transaction status is not valid.");
        }
    }
}
