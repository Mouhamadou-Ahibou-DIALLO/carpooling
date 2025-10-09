package api.carpooling.application.dto.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link LoginUserRequest} DTO.
 * <p>
 * These tests verify that the record correctly stores values
 * and that validation constraints (@Email, @NotBlank) work as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Login User Request Test")
public class LoginUserRequestTest {

    /**
     * Shared Validator instance used for bean validation (Jakarta Validation API).
     */
    private static Validator validator;

    /**
     * Initializes a validator before all tests.
     */
    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        System.out.println("Validator initialized");
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
     * Verifies that a valid LoginUserRequest passes validation.
     */
    @Test
    @Order(1)
    @DisplayName("Should verify that a valid LoginUserRequest passes validation")
    void testValidLoginRequest() {
        LoginUserRequest request = new LoginUserRequest("john@example.com", "securePass123");

        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "No constraint violations should occur for valid data");

        assertEquals("john@example.com", request.email());
        assertEquals("securePass123", request.password());
    }

    /**
     * Verifies that email cannot be blank or invalid.
     */
    @Test
    @Order(2)
    @DisplayName("Should verify that email cannot be blank or invalid")
    void testInvalidEmail() {
        LoginUserRequest request = new LoginUserRequest("invalid-email", "securePass123");

        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid email should trigger constraint violation");

        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(hasEmailViolation, "Violation should be detected on the email field");
    }

    /**
     * Verifies that password cannot be blank.
     */
    @Test
    @Order(3)
    @DisplayName("Should verify that password cannot be blank")
    void testBlankPassword() {
        LoginUserRequest request = new LoginUserRequest("john@example.com", "");

        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Blank password should trigger constraint violation");

        boolean hasPasswordViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(hasPasswordViolation, "Violation should be detected on the password field");
    }

    /**
     * Verifies that both fields are invalid at once.
     */
    @Test
    @Order(4)
    @DisplayName("Should verify that both fields are invalid at once")
    void testBothFieldsInvalid() {
        LoginUserRequest request = new LoginUserRequest("", "");

        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(request);
        assertEquals(2, violations.size(), "Two violations expected (email + password)");
    }
}
