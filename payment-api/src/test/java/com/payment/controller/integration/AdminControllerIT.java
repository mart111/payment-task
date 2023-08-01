package com.payment.controller.integration;

import com.payment.model.Role;
import com.payment.model.Status;
import com.payment.model.TransactionStatus;
import com.payment.model.entity.AuthorizeTransaction;
import com.payment.model.entity.Merchant;
import com.payment.model.entity.Transaction;
import com.payment.model.request.MerchantEditRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.*;

public class AdminControllerIT extends BaseIntegrationTest {

    @Test
    @WithMockUser(roles = "admin")
    void fetchAllMerchants() {
        saveMerchant();
        given()
                .when()
                .get("/merchants")
                .then()
                .statusCode(200)
                .body(".", is(notNullValue()))
                .body("merchantResponseList.size()", equalTo(1))
                .body("merchantResponseList[0].name", equalTo("test"))
                .body("merchantResponseList[0].id", notNullValue());
    }

    @Test
    @WithMockUser(roles = "admin")
    void fetchMerchantTransactions() {
        saveMerchant();
        saveTransactions();
        given()
                .param("merchantEmail", "test@gmail.com")
                .get("/merchants/transactions")
                .then()
                .statusCode(200)
                .body(".", notNullValue())
                .body("transactionResponseList.size()", equalTo(1))
                .body("transactionResponseList[0].status",
                        equalToIgnoringCase(TransactionStatus.AUTHORIZED.getStatusName()));
    }

    @Test
    @WithAnonymousUser
    void fetchMerchantTransactions_shouldThrowIfNotAdmin() {
        saveMerchant();
        saveTransactions();
        given()
                .param("merchantEmail", "test@gmail.com")
                .get("/merchants/transactions")
                .then()
                .statusCode(401);
    }

    @Test
    @WithMockUser(roles = "admin")
    void fetchMerchantTransactions_MerchantNotFound() {
        saveMerchant();
        saveTransactions();

        given()
                .param("merchantEmail", "test-wrong@gmail.com")
                .get("/merchants/transactions")
                .then()
                .statusCode(200)
                .body("transactionResponseList.size()", equalTo(0));
    }

    @Test
    @WithMockUser(roles = "admin")
    void importMerchants() throws URISyntaxException {
        File csvFile =
                Paths.get(AdminControllerIT.class
                                .getResource("/csv/users.csv")
                                .toURI())
                        .toFile();
        given()
                .multiPart("file",
                        csvFile,
                        "text/csv")
                .post("/merchants/import")
                .then()
                .statusCode(equalTo(201))
                .body(".", notNullValue())
                .body("[0].name", equalTo("Merchant1"))
                .body("[1].name", equalTo("Merchant2"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void importMerchants_DuplicateUsername() throws URISyntaxException {
        File csvFile =
                Paths.get(AdminControllerIT.class
                                .getResource("/csv/users-duplicate.csv")
                                .toURI())
                        .toFile();
        given()
                .multiPart("file",
                        csvFile,
                        "text/csv")
                .post("/merchants/import")
                .then()
                .statusCode(equalTo(409))
                .body("username", notNullValue());
    }

    @Test
    @WithMockUser(roles = "admin")
    void importMerchants_RequiredHeaderValuesAreMissing() throws URISyntaxException {
        File csvFile =
                Paths.get(AdminControllerIT.class
                                .getResource("/csv/users-missing-mandatory-field.csv")
                                .toURI())
                        .toFile();
        given()
                .multiPart("file",
                        csvFile,
                        "text/csv")
                .post("/merchants/import")
                .then()
                .statusCode(equalTo(400))
                .body("errorMessage", containsStringIgnoringCase("'name, email, password, role'"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void importMerchants_FileIsMissing() throws URISyntaxException {
        File csvFile =
                Paths.get(AdminControllerIT.class
                                .getResource("/csv/empty-users.csv")
                                .toURI())
                        .toFile();
        given()
                .multiPart("file", csvFile, "text/csv")
                .post("/merchants/import")
                .then()
                .statusCode(equalTo(400))
                .body("errorMessage", equalTo("Provided CSV file is empty or is not a valid CSV file."));
    }

    @Test
    @WithMockUser(roles = "admin")
    void updateMerchant() {

        final var merchant = saveMerchant();
        MerchantEditRequest merchantEditRequest = new MerchantEditRequest(
                "Updated", "Updated",
                "update@gmail.com",
                null,
                null
        );
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(merchantEditRequest)
                .when()
                .put("/merchants/{merchantId}", merchant.getId())
                .then()
                .statusCode(is(200))
                .body("name", equalTo("Updated"))
                .body("email", equalTo("update@gmail.com"))
                .body("description", equalTo("Updated"))
                .body("status", equalTo(merchant.getStatus().getStatusName()));
    }

    @Test
    @WithMockUser(roles = "admin")
    void updateMerchant_MerchantNotFound() {

        MerchantEditRequest merchantEditRequest = new MerchantEditRequest(
                "Updated", "Updated",
                "update@gmail.com",
                null,
                null
        );
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(merchantEditRequest)
                .when()
                .put("/merchants/{merchantId}", 200)
                .then()
                .statusCode(is(400))
                .body("errorMessage", containsStringIgnoringCase("Failed to update merchant"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void deleteMerchant() {
        final var merchant = saveMerchant();

        when()
                .delete("/merchants/{merchantId}", merchant.getId())
                .then()
                .statusCode(is(204));

        nullValue().matches((merchantRepository.findById(merchant.getId())
                .orElse(null)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void deleteMerchant_WithTransactionHistory() {
        final var merchant = saveMerchant();
        saveTransactions();

        when()
                .delete("/merchants/{merchantId}", merchant.getId())
                .then()
                .statusCode(is(400))
                .body("errorMessage",
                        equalToIgnoringCase("Merchant can't be deleted, due to related payment history."));
    }

    Merchant saveMerchant() {
        Merchant merchant = new Merchant();
        merchant.setRole(Role.MERCHANT);
        merchant.setEmail("test@gmail.com");
        merchant.setPassword("Test1234");
        merchant.setName("test");
        merchant.setStatus(Status.ACTIVE);
        return merchantRepository.save(merchant);
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
