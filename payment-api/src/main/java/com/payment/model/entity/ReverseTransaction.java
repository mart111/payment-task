package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("REVERSE")
public class ReverseTransaction extends Transaction {

    public ReverseTransaction() {
        super();
    }

    @Override
    protected void setStatus() {
        transactionStatus = TransactionStatus.REVERSED;
    }

    @Override
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
