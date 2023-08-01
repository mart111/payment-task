package com.payment.controller.integration;

import com.payment.model.Role;
import com.payment.model.request.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;

public class RegistrationControllerIT extends BaseIntegrationTest {

    @Test
    void register() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test@gmail.com",
                "Test12345",
                Role.MERCHANT,
                "Test Name",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.CREATED)
                .body("email", equalTo("test@gmail.com"))
                .body("name", equalTo("Test Name"))
                .body("description", equalTo("Test Description"))
                .body("role", equalTo(Role.MERCHANT.getRoleName()));

    }

    @Test
    void register_notValidEmail() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test@",
                "Test12345",
                Role.MERCHANT,
                "Test Name",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(1))
                .body("errors.username", startsWithIgnoringCase("Email address is not valid"));

    }

    @Test
    void register_notValidPassword() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test@gmail.ru",
                "Te",
                Role.MERCHANT,
                "Test Name",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(1))
                .body("errors.password", startsWithIgnoringCase("Password should contain at least 6 characters"));

    }

    @Test
    void register_UsernameAndPasswordAreNotValid() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test",
                "Te",
                Role.MERCHANT,
                "Test Name",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(2))
                .body("errors.username", startsWithIgnoringCase("Email address is not valid"))
                .body("errors.password", startsWithIgnoringCase("Password should contain at least 6 characters"));

    }

    @Test
    void register_UsernameAndPasswordAreNull() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                null,
                null,
                Role.MERCHANT,
                "Test Name",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(2))
                .body("errors.username", startsWithIgnoringCase("This field is required"))
                .body("errors.password", startsWithIgnoringCase("This field is required"));

    }

    @Test
    void register_RoleIsNull() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test@gmail.com",
                "Test12345",
                null,
                "Test Name",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(1))
                .body("errors.role", startsWithIgnoringCase("This field is required"));

    }

    @Test
    void register_NameIsNull() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test@gmail.com",
                "Test12345",
                Role.MERCHANT,
                null,
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(1))
                .body("errors.name", startsWithIgnoringCase("This field is required"));

    }

    @Test
    void register_NameLengthLessThanThree() {
        UserRegistrationRequest registrationRequest
                = new UserRegistrationRequest(
                "test@gmail.com",
                "Test12345",
                Role.MERCHANT,
                "Ab",
                "Test Description"
        );

        given()
                .body(registrationRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/register")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("error", containsStringIgnoringCase("Validation failed"))
                .body("count", equalTo(1))
                .body("errors.name", startsWithIgnoringCase("Name should contain at least 3 characters"));

    }
}
