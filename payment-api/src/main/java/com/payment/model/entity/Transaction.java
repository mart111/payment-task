package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
@Table
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    @Setter(AccessLevel.NONE)
    protected UUID id;

    protected BigDecimal amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected TransactionStatus transactionStatus;

    @Column(name = "customer_email", nullable = false)
    protected String customerEmail;

    @Column(name = "customer_phone")
    protected String customerPhone;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "creation_date", nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(name = "reference_id")
    protected String referenceId;

    public Transaction() {
        this.createdAt = Instant.now();
        setStatus();
    }

    protected abstract void setStatus();

    public abstract void setReferenceId(String referenceId);

}
