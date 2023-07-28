package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "transaction_id")
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
