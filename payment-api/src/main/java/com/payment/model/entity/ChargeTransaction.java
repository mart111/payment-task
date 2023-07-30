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

    public static ChargeTransaction wrap(Transaction toWrap) {
        ChargeTransaction transaction = new ChargeTransaction();
        transaction.setAmount(toWrap.amount);
        transaction.setCustomerEmail(toWrap.customerEmail);
        transaction.setCustomerPhone(toWrap.customerPhone);
        transaction.setReferenceId(toWrap.getReferenceId());
        transaction.setMerchantId(toWrap.getMerchantId());

        return transaction;
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
