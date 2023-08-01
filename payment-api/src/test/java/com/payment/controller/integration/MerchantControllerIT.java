package com.payment.controller.integration;

import com.payment.model.Role;
import com.payment.model.Status;
import com.payment.model.TransactionStatus;
import com.payment.model.entity.AuthorizeTransaction;
import com.payment.model.entity.Merchant;
import com.payment.model.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class MerchantControllerIT extends BaseIntegrationTest {

    @Test
    @WithMockUser(roles = "merchant",
            username = "test@gmail.com")
    void getAllTransactions() {
        saveMerchant();
        saveTransactions();

        when()
                .get("/merchant/transactions")
                .then()
                .status(HttpStatus.OK)
                .body("transactionResponseList.size()", equalTo(1))
                .body("transactionResponseList[0].customerEmail", equalTo("test-customer@gmail.com"))
                .body("transactionResponseList[0].transactionStatus",
                        equalToIgnoringCase(TransactionStatus.AUTHORIZED.getStatusName()));

    }

    @Test
    @WithMockUser(roles = "merchant",
            username = "test@gmail.com")
    void getAllTransactions_NoTransactions() {
        saveMerchant();
        when()
                .get("/merchant/transactions")
                .then()
                .status(HttpStatus.OK)
                .body("transactionResponseList.size()", equalTo(0));
    }

    @Test
    @WithAnonymousUser
    void getAllTransactions_NoAuth() {
        saveMerchant();
        when()
                .get("/merchant/transactions")
                .then()
                .status(HttpStatus.UNAUTHORIZED);
    }

    void saveMerchant() {
        Merchant merchant = new Merchant();
        merchant.setRole(Role.MERCHANT);
        merchant.setEmail("test@gmail.com");
        merchant.setPassword("Test1234");
        merchant.setName("test");
        merchant.setStatus(Status.ACTIVE);
        merchantRepository.save(merchant);
    }

    void saveTransactions() {
        final var merchant = merchantRepository.findMerchantByEmail("test@gmail.com")
                .get();
        Transaction transaction = new AuthorizeTransaction();
        transaction.setAmount(BigDecimal.TEN);
        transaction.setCustomerEmail("test-customer@gmail.com");
        merchant.addTransaction(transaction);
        merchantRepository.save(merchant);
    }
}
