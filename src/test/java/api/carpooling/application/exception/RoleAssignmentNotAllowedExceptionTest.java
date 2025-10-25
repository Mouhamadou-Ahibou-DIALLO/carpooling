package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RoleAssignmentNotAllowedException}.
 * <p>
 * These tests verify that the exception correctly stores its message,
 * associates the expected {@link ErrorCode#FORBIDDEN}, and returns
 * the correct {@link HttpStatus#FORBIDDEN}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("RoleAssignmentNotAllowedException Test")
@Slf4j
public class RoleAssignmentNotAllowedExceptionTest {

    /**
     * Message used when a role assignment is not allowed.
     */
    private static final String MESSAGE = "Role assignment not allowed";

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting RoleAssignmentNotAllowedException tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished RoleAssignmentNotAllowedException tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        RoleAssignmentNotAllowedException ex = new RoleAssignmentNotAllowedException(MESSAGE);

        assertAll("RoleAssignmentNotAllowedException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.FORBIDDEN, ex.getErrorCode(), "ErrorCode should match"),
                () -> assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus(), "HttpStatus should match")
        );
    }

    /**
     * Verifies that the exception is an instance of {@link ApiException} and {@link RuntimeException}.
     */
    @Test
    @Order(2)
    @DisplayName("Should be a subtype of ApiException and RuntimeException")
    void testInstanceOf() {
        RoleAssignmentNotAllowedException ex = new RoleAssignmentNotAllowedException(MESSAGE);

        assertAll("Instance type checks",
                () -> assertInstanceOf(ApiException.class, ex, "Should be an ApiException"),
                () -> assertTrue(true, "Should be a RuntimeException")
        );
    }
}
