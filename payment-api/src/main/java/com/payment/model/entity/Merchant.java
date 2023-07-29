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
public class Merchant extends User {

    @Column(name = "total_transaction_sum")
    private BigDecimal totalTransactionSum;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "merchant_transaction",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id",
                    referencedColumnName = "transaction_id"))
    @Setter(AccessLevel.NONE)
    private List<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }
        this.transactions.add(transaction);
    }
}
