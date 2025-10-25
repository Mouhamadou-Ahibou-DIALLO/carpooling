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
 * Unit tests for {@link UserNotActiveException}.
 * <p>
 * These tests verify that the exception correctly stores the message,
 * associates the proper error code {@link ErrorCode#USER_NOT_ACTIVE},
 * and returns the expected HTTP status {@link HttpStatus#UNAUTHORIZED}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserNotActiveException Test")
@Slf4j
public class UserNotActiveExceptionTest {

    /**
     * Message used when a user is not active.
     */
    private static final String MESSAGE = "User is not active";

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting UserNotActiveException tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished UserNotActiveException tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        UserNotActiveException ex = new UserNotActiveException(MESSAGE);

        assertAll("UserNotActiveException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.USER_NOT_ACTIVE, ex.getErrorCode(), "ErrorCode should match"),
                () -> assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus(), "HttpStatus should match")
        );
    }

    /**
     * Verifies that the exception is an instance of ApiException and RuntimeException.
     */
    @Test
    @Order(2)
    @DisplayName("Should be a subtype of ApiException and RuntimeException")
    void testInstanceOf() {
        UserNotActiveException ex = new UserNotActiveException(MESSAGE);
        assertInstanceOf(ApiException.class, ex, "Should be an ApiException");
        assertTrue(true, "Should be a RuntimeException");
    }
}
