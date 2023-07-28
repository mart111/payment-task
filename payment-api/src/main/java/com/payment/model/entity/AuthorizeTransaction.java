package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AUTHORIZED")
public class AuthorizeTransaction extends Transaction {

    @Override
    protected void setStatus() {
        transactionStatus = TransactionStatus.AUTHORIZED;
    }

    @Override
    public void setReferenceId(String referenceId) {
        // noop
    }
}
