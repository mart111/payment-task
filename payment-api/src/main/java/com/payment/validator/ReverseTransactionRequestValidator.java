package com.payment.validator;

import com.payment.model.TransactionStatus;
import com.payment.model.request.ReverseTransactionRequest;
import com.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReverseTransactionRequestValidator implements Validator {

    private final TransactionRepository transactionRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ReverseTransactionRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final var reverseTransactionRequest = (ReverseTransactionRequest) target;
        final var authorizedTransactionId = reverseTransactionRequest.getAuthorizedTransactionId();
        final var transaction = transactionRepository.findById(UUID.fromString(authorizedTransactionId))
                .orElse(null);
        if (transaction == null) {
            errors.rejectValue("authorizedTransactionId", "Couldn't find transaction.");
            return;
        }
        if (TransactionStatus.AUTHORIZED != transaction.getTransactionStatus()) {
            errors.rejectValue("authorizedTransactionId", String.format(
                    "Couldn't refund Transaction with status '%s'", authorizedTransactionId
            ));
        }
    }
}
