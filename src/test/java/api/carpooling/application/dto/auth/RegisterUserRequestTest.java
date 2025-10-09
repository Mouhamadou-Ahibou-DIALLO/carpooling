package api.carpooling.application.dto.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RegisterUserRequest} DTO.
 * <p>
 * These tests verify that validation constraints (@Email, @NotBlank, @Pattern)
 * behave correctly and that the record stores values as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Register User Request Test")
public class RegisterUserRequestTest {

    /**
     * Shared Validator instance used for bean validation (Jakarta Validation API).
     */
    private static Validator validator;

    /**
     * Initializes the validator before all tests.
     */
    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        System.out.println("Validator initialized for RegisterUserRequest");
    }

    /**
     * Clears the validator after all tests.
     */
    @AfterAll
    static void tearDownValidator() {
        validator = null;
        System.out.println("Validator cleared");
    }

    /**
     * Verifies that a valid request passes all validation constraints.
     */
    @Test
    @Order(1)
    @DisplayName("Should verify that a valid request passes all validation constraints")
    void testValidRegisterUserRequest() {
        RegisterUserRequest request = new RegisterUserRequest(
                "john_doe",
                "john@example.com",
                "StrongPass1@",
                "+33612345678"
        );

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "No constraint violations should occur for valid data");

        assertEquals("john_doe", request.username());
        assertEquals("john@example.com", request.email());
        assertEquals("StrongPass1@", request.password());
        assertEquals("+33612345678", request.phoneNumber());
    }

    /**
     * Verifies that email must be valid and not blank.
     */
    @Test
    @Order(2)
    @DisplayName("Should verify that email must be valid and not blank")
    void testInvalidEmail() {
        RegisterUserRequest request = new RegisterUserRequest(
                "john_doe",
                "invalid-email",
                "StrongPass1@",
                "+33612345678"
        );

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid email should trigger constraint violation");

        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(hasEmailViolation, "Violation should be detected on the email field");
    }

    /**
     * Verifies that password must respect the required pattern.
     */
    @Test
    @Order(3)
    @DisplayName("Should verify that password must respect the required pattern")
    void testWeakPassword() {
        RegisterUserRequest request = new RegisterUserRequest(
                "john_doe",
                "john@example.com",
                "weakpass",  // missing uppercase, digit, special char
                "+33612345678"
        );

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Weak password should trigger constraint violation");

        boolean hasPasswordViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(hasPasswordViolation, "Violation should be detected on the password field");
    }

    /**
     * Verifies that phone number must match international format.
     */
    @Test
    @Order(4)
    @DisplayName("Should verify that phone number must match international format")
    void testInvalidPhoneNumber() {
        RegisterUserRequest request = new RegisterUserRequest(
                "john_doe",
                "john@example.com",
                "StrongPass1@",
                "0612345678" // missing '+' and country code
        );

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid phone number should trigger constraint violation");

        boolean hasPhoneViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
        assertTrue(hasPhoneViolation, "Violation should be detected on the phoneNumber field");
    }

    /**
     * Verifies that all fields being blank triggers multiple violations.
     */
    @Test
    @Order(5)
    @DisplayName("Should verify that all fields being blank triggers multiple violations")
    void testAllFieldsBlank() {
        RegisterUserRequest request = new RegisterUserRequest("", "", "", "");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);
        assertEquals(6, violations.size(), "Expected 6 violations (username, email, password, phoneNumber)");
    }
}
