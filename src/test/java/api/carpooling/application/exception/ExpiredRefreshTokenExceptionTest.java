package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ExpiredRefreshTokenException}.
 * <p>
 * These tests verify that the exception correctly stores the message,
 * associates the proper error code {@link ErrorCode#AUTH_TOKEN_EXPIRED},
 * and returns the expected HTTP status {@link HttpStatus#BAD_REQUEST}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ExpiredRefreshTokenException Test")
@Slf4j
class ExpiredRefreshTokenExceptionTest {

    /**
     * Message used when a refresh token has expired.
     */
    private static final String MESSAGE = "Refresh token has expired";

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting ExpiredRefreshTokenException tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished ExpiredRefreshTokenException tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        ExpiredRefreshTokenException ex = new ExpiredRefreshTokenException(MESSAGE);

        assertAll("ExpiredRefreshTokenException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.AUTH_TOKEN_EXPIRED, ex.getErrorCode(), "ErrorCode should match"),
                () -> assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus(), "HttpStatus should match")
        );
    }

    /**
     * Verifies that the exception is an instance of ApiException and RuntimeException.
     */
    @Test
    @Order(2)
    @DisplayName("Should be a subtype of ApiException and RuntimeException")
    void testInstanceOf() {
        ExpiredRefreshTokenException ex = new ExpiredRefreshTokenException(MESSAGE);
        assertInstanceOf(ApiException.class, ex, "Should be an ApiException");
        assertTrue(true, "Should be a RuntimeException");
    }
}
