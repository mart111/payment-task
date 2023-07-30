package com.payment.repository;

import com.payment.model.entity.Merchant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @EntityGraph("merchant_entity_graph")
    Optional<Merchant> findMerchantByEmail(String email);
}
