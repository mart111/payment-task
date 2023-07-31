package com.payment.validator;

import com.payment.model.TransactionStatus;
import com.payment.model.entity.Transaction;
import com.payment.model.request.ReverseTransactionRequest;
import com.payment.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReverseTransactionRequestValidatorTest {

    private static final ReverseTransactionRequest REVERSE_TRANSACTION_REQUEST =
            new ReverseTransactionRequest(
                    UUID.randomUUID().toString()
            );

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    Errors errors;

    @InjectMocks
    ReverseTransactionRequestValidator testSubject;

    @Test
    void supports() {
        assertTrue(testSubject.supports(REVERSE_TRANSACTION_REQUEST.getClass()));
    }

    @Test
    void validate() {
        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(new Transaction() {
                    @Override
                    protected void setStatus() {
                        this.transactionStatus = TransactionStatus.AUTHORIZED;
                    }

                    @Override
                    public void setReferenceId(String referenceId) {
                        // noop
                    }
                }));
        testSubject.validate(REVERSE_TRANSACTION_REQUEST, errors);
        verify(errors, never())
                .rejectValue(anyString(), anyString());
    }

    @Test
    void validate_AuthorizedTransactionIsNull() {
        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        testSubject.validate(REVERSE_TRANSACTION_REQUEST, errors);
        verify(errors)
                .rejectValue(anyString(), anyString());
    }

    @Test
    void validate_ReturnedTransactionIsNotAuthorized() {
        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(new Transaction() {
                    @Override
                    protected void setStatus() {
                        this.transactionStatus = TransactionStatus.APPROVED;
                    }

                    @Override
                    public void setReferenceId(String referenceId) {
                        // noop
                    }
                }));
        testSubject.validate(REVERSE_TRANSACTION_REQUEST, errors);
        verify(errors)
                .rejectValue(anyString(), anyString());
    }
}