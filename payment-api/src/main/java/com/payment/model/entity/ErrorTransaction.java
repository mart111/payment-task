package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ERROR")
public class ErrorTransaction extends Transaction {

    public ErrorTransaction() {
        super();
    }

    @Override
    protected void setStatus() {
        transactionStatus = TransactionStatus.ERROR;
    }

    @Override
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
