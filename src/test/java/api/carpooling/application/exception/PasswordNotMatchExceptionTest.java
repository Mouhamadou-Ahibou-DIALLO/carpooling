package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PasswordNotMatchException}.
 * <p>
 * These tests verify that the exception correctly stores the message,
 * associates the proper error code {@link ErrorCode#AUTH_PASSWORD_INVALID},
 * and returns the expected HTTP status {@link HttpStatus#BAD_REQUEST}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("PasswordNotMatchException Test")
class PasswordNotMatchExceptionTest {

    private static final String MESSAGE = "Password does not match";

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting PasswordNotMatchException tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished PasswordNotMatchException tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        PasswordNotMatchException ex = new PasswordNotMatchException(MESSAGE);

        assertAll("PasswordNotMatchException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.AUTH_PASSWORD_INVALID, ex.getErrorCode(), "ErrorCode should match"),
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
        PasswordNotMatchException ex = new PasswordNotMatchException(MESSAGE);
        assertInstanceOf(ApiException.class, ex, "Should be an ApiException");
        assertTrue(true, "Should be a RuntimeException");
    }
}
