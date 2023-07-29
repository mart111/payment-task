package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHARGE")
public class ChargeTransaction extends Transaction {

    public ChargeTransaction() {
        super();
    }

    @Override
    protected void setStatus() {
        transactionStatus = TransactionStatus.APPROVED;
    }

    @Override
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
