package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "transaction_id")
public class ChargeTransaction extends Transaction {

    @Column(name = "reference_id")
    protected String referenceId;

    @Override
    protected void setStatus() {
        transactionStatus = TransactionStatus.APPROVED;
    }

    @Override
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
