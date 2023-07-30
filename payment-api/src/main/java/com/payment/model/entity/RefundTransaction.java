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

    public static RefundTransaction wrap(Transaction toWrap) {
        RefundTransaction transaction = new RefundTransaction();
        transaction.setAmount(toWrap.amount);
        transaction.setCustomerEmail(toWrap.customerEmail);
        transaction.setCustomerPhone(toWrap.customerPhone);
        transaction.setReferenceId(toWrap.getReferenceId());

        return transaction;
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
