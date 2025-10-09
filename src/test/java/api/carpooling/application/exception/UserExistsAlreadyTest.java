package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserExistsAlready}.
 * <p>
 * These tests verify that the exception correctly stores the message,
 * associates the proper error code {@link ErrorCode#USER_FOUND},
 * and returns the expected HTTP status {@link HttpStatus#FOUND}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserExistsAlready Test")
class UserExistsAlreadyTest {

    private static final String MESSAGE = "User already exists";

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting UserExistsAlready tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished UserExistsAlready tests");
    }

    /**
     * Verifies that the exception correctly stores the message, error code, and HTTP status.
     */
    @Test
    @Order(1)
    @DisplayName("Should store message, error code, and HTTP status correctly")
    void testExceptionFields() {
        UserExistsAlready ex = new UserExistsAlready(MESSAGE);

        assertAll("UserExistsAlready fields",
                () -> assertEquals(MESSAGE, ex.getMessage(), "Message should match"),
                () -> assertEquals(ErrorCode.USER_FOUND, ex.getErrorCode(), "ErrorCode should match"),
                () -> assertEquals(HttpStatus.FOUND, ex.getHttpStatus(), "HttpStatus should match")
        );
    }

    /**
     * Verifies that the exception is an instance of ApiException and RuntimeException.
     */
    @Test
    @Order(2)
    @DisplayName("Should be a subtype of ApiException and RuntimeException")
    void testInstanceOf() {
        UserExistsAlready ex = new UserExistsAlready(MESSAGE);
        assertInstanceOf(ApiException.class, ex, "Should be an ApiException");
        assertInstanceOf(RuntimeException.class, ex, "Should be a RuntimeException");
    }
}
