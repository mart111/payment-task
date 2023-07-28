package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    @Setter(AccessLevel.NONE)
    protected UUID id;

    protected BigDecimal amount;

    @Column(name = "status")
    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    protected TransactionStatus transactionStatus;

    @Column(name = "customer_email")
    protected String customerEmail;

    @Column(name = "customer_phone")
    protected String customerPhone;

    public Transaction() {
        setStatus();
    }

    protected abstract void setStatus();

    public abstract void setReferenceId(String referenceId);

}
