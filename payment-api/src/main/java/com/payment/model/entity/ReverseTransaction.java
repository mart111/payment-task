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

    public static ReverseTransaction wrap(Transaction toWrap) {
        ReverseTransaction transaction = new ReverseTransaction();
        transaction.setAmount(toWrap.amount);
        transaction.setCustomerEmail(toWrap.customerEmail);
        transaction.setCustomerPhone(toWrap.customerPhone);
        transaction.setReferenceId(toWrap.getReferenceId());

        return transaction;
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
