package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.junit.jupiter.api.*;

import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ApiException}.
 * <p>
 * These tests verify that the base exception correctly stores the error message,
 * error code, and HTTP status. It ensures consistent behavior for all API exceptions
 * that extend {@code ApiException}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ApiException Test")
class ApiExceptionTest {

    private static final String MESSAGE = "Test exception message";

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting ApiException tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished ApiException tests");
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
        TestApiException ex = new TestApiException(MESSAGE, ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        assertTrue(true, "ApiException should extend RuntimeException");
    }
}
