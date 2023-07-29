package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("REFUND")
public class RefundTransaction extends Transaction {

    public RefundTransaction() {
        super();
    }

    @Override
    protected void setStatus() {
        transactionStatus = TransactionStatus.REFUNDED;
    }

    @Override
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
