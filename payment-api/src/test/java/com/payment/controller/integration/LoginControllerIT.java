package com.payment.controller.integration;

import com.payment.model.Role;
import com.payment.model.request.LoginRequest;
import com.payment.model.request.UserRegistrationRequest;
import com.payment.service.RegistrationService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;

public class LoginControllerIT extends BaseIntegrationTest {

    @Autowired
    RegistrationService registrationService;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest("test@gmail.com",
                "Test1234");
        saveUser();
        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username", equalTo("test@gmail.com"))
                .body("authenticationToken.authToken", is(notNullValue()));

    }

    @Test
    void testLogin_CredentialsAreNotCorrect() {
        LoginRequest loginRequest = new LoginRequest("test@gmail.com",
                "WrongPassword");
        saveUser();
        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

    }

    void saveUser() {
        UserRegistrationRequest user = new UserRegistrationRequest();
        user.setRole(Role.MERCHANT);
        user.setUsername("test@gmail.com");
        user.setPassword("Test1234");
        user.setName("test");
        user.setRole(Role.ADMIN);
        registrationService.register(user);
    }
}
