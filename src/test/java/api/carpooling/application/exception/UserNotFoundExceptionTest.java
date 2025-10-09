package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserNotFoundException}.
 * <p>
 * These tests verify that the exception correctly stores the message,
 * associates the proper error code {@link ErrorCode#USER_NOT_FOUND},
 * and returns the expected HTTP status {@link HttpStatus#NOT_FOUND}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserNotFoundException Test")
class UserNotFoundExceptionTest {

    private static final String MESSAGE = "User not found";

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting UserNotFoundException tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished UserNotFoundException tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        UserNotFoundException ex = new UserNotFoundException(MESSAGE);

        assertAll("UserNotFoundException fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode(), "ErrorCode should match"),
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
        UserNotFoundException ex = new UserNotFoundException(MESSAGE);
        assertInstanceOf(ApiException.class, ex, "Should be an ApiException");
        assertInstanceOf(RuntimeException.class, ex, "Should be a RuntimeException");
    }
}
