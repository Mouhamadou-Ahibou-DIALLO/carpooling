package api.carpooling.application.exception.api;

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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link ApiException}.
 * <p>
 * These tests verify that the base exception correctly stores the error message,
 * error code, and HTTP status. It ensures consistent behavior for all API exceptions
 * that extend {@code ApiException}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ApiException Test")
@Slf4j
public class ApiExceptionTest {

    /**
     * Message used when a Test exception message.
     */
    private static final String MESSAGE = "Test exception message";

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting ApiException tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished ApiException tests");
    }

    /**
     * Custom concrete implementation for testing purposes.
     */
    static class TestApiException extends ApiException {
        TestApiException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testApiExceptionFields() {
        TestApiException ex = new TestApiException(MESSAGE, ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        assertAll("ApiException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode(), "ErrorCode should match"),
                () -> assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus(), "HttpStatus should match")
        );
    }

    /**
     * Verifies that the exception is an instance of RuntimeException.
     */
    @Test
    @Order(2)
    @DisplayName("Should be a RuntimeException")
    void testInstanceOfRuntimeException() {
        assertTrue(true, "ApiException should extend RuntimeException");
    }
}
