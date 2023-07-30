package com.payment.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("merchant")
@NamedEntityGraph(name = "merchant_entity_graph",
        attributeNodes = @NamedAttributeNode("transactions"))
public class Merchant extends User {

    @Column(name = "total_transaction_sum")
    @Setter(AccessLevel.NONE)
    private BigDecimal totalTransactionSum;

    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "merchant_transaction",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id",
                    referencedColumnName = "transaction_id"))
    private List<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }
        this.transactions.add(transaction);
    }

    public void addToTotalSum(BigDecimal amount) {
        if (this.totalTransactionSum == null) {
            this.totalTransactionSum = BigDecimal.ZERO;
        }
        totalTransactionSum = this.totalTransactionSum.add(amount);
    }

    public void subtractFromTotalSum(BigDecimal amount) {
        if (this.totalTransactionSum == null) {
            this.totalTransactionSum = BigDecimal.ZERO;
        }
        totalTransactionSum = this.totalTransactionSum.subtract(amount);
    }
}
