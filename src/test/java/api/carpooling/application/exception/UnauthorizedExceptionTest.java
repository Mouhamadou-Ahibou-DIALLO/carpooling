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

/**
 * Unit tests for {@link UnauthorizedException}.
 * <p>
 * These tests verify that the exception correctly stores the message,
 * associates the proper error code {@link ErrorCode#INVALID_TOKEN},
 * and returns the expected HTTP status {@link HttpStatus#NOT_FOUND}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UnauthorizedException Test")
@Slf4j
public class UnauthorizedExceptionTest {

    /**
     * Message used when a token is invalid.
     */
    private static final String MESSAGE = "Invalid token provided";

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting UnauthorizedException tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished UnauthorizedException tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        UnauthorizedException ex = new UnauthorizedException(MESSAGE);

        assertAll("UnauthorizedException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.INVALID_TOKEN, ex.getErrorCode(), "ErrorCode should match"),
                () -> assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus(), "HttpStatus should match")
        );
    }

    /**
     * Verifies that the exception is an instance of ApiException and RuntimeException.
     */
    @Test
    @Order(2)
    @DisplayName("Should be a subtype of ApiException and RuntimeException")
    void testInstanceOf() {
        UnauthorizedException ex = new UnauthorizedException(MESSAGE);

        assertAll("UnauthorizedException inheritance",
                () -> assertInstanceOf(ApiException.class, ex, "Should be an ApiException"),
                () -> assertInstanceOf(RuntimeException.class, ex, "Should be a RuntimeException")
        );
    }
}
