package com.payment.validator;

import com.payment.model.TransactionStatus;
import com.payment.model.entity.Transaction;
import com.payment.model.request.RefundTransactionRequest;
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
class RefundTransactionRequestValidatorTest {

    private static final RefundTransactionRequest REFUND_TRANSACTION_REQUEST =
            new RefundTransactionRequest(
                    UUID.randomUUID().toString()
            );

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    Errors errors;

    @InjectMocks
    RefundTransactionRequestValidator testSubject;

    @Test
    void supports() {
        assertTrue(testSubject.supports(REFUND_TRANSACTION_REQUEST.getClass()));
    }

    @Test
    void validate() {
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
        testSubject.validate(REFUND_TRANSACTION_REQUEST, errors);
        verify(errors, never())
                .rejectValue(anyString(), anyString());
    }

    @Test
    void validate_AuthorizedTransactionIsNull() {
        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        testSubject.validate(REFUND_TRANSACTION_REQUEST, errors);
        verify(errors)
                .rejectValue(anyString(), anyString());
    }

    @Test
    void validate_ReturnedTransactionIsNotAuthorized() {
        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(new Transaction() {
                    @Override
                    protected void setStatus() {
                        this.transactionStatus = TransactionStatus.REVERSED;
                    }

                    @Override
                    public void setReferenceId(String referenceId) {
                        // noop
                    }
                }));
        testSubject.validate(REFUND_TRANSACTION_REQUEST, errors);
        verify(errors)
                .rejectValue(anyString(), anyString());
    }
}