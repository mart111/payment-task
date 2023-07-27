package com.payment.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("merchant")
public class Merchant extends User {

    @Column(name = "total_transaction_sum")
    private BigDecimal totalTransactionSum;
}
