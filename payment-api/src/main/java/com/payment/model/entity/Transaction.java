package com.payment.model.entity;

import com.payment.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
@Table(indexes = @Index(
        name = "tx_number_idx",
        columnList = "transaction_number"
))
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    @Setter(AccessLevel.NONE)
    protected UUID id;

    @Column(name = "transaction_number", nullable = false)
    @UuidGenerator
    protected UUID transactionNumber; // acts like id. The purpose of this field is,
    // prevention of exposing actual transaction_id.

    protected BigDecimal amount;

    @Column(name = "status")
    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    protected TransactionStatus transactionStatus;

    @Column(name = "customer_email", nullable = false)
    protected String customerEmail;

    @Column(name = "customer_phone")
    protected String customerPhone;

    @Column(name = "creation_date", nullable = false)
    private Instant createdAt;

    @Column(name = "reference_id")
    protected String referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "merchant_transaction",
            joinColumns = @JoinColumn(name = "transaction_id",
                    referencedColumnName = "transaction_id",
                    insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id",
                    insertable = false, updatable = false))
    @ToString.Exclude
    protected Merchant merchant;

    public Transaction() {
        this.createdAt = Instant.now();
        this.transactionNumber = UUID.randomUUID();
        setStatus();
    }

    protected abstract void setStatus();

    public abstract void setReferenceId(String referenceId);

}
