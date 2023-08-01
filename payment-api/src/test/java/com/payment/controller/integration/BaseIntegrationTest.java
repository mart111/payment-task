package com.payment.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.repository.MerchantRepository;
import com.payment.repository.TransactionRepository;
import com.payment.repository.UserRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        properties = "spring.application.name=be-payment",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integrationtest")
public class BaseIntegrationTest {

    @LocalServerPort
    Integer port;


    static MySQLContainer<?> mySQLContainer;

    static {
        mySQLContainer =
                new MySQLContainer<>("mysql:8.0");

        mySQLContainer.start();
    }

    @Autowired
    protected MerchantRepository merchantRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TransactionRepository transactionRepository;


    @Autowired
    protected ObjectMapper objectMapper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        merchantRepository.deleteAll();
        transactionRepository.deleteAll();
        RestAssuredMockMvc.basePath = String.format("http://localhost:%s/api/v1", port);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
}
