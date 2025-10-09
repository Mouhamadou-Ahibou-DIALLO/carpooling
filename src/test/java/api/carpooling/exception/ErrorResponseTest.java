package api.carpooling.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit tests for {@link ErrorResponse} DTO.
 * <p>
 * These tests verify that the builder correctly assigns values,
 * that getters and setters work, and that the nested {@link ErrorResponse.ValidationError}
 * class behaves as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ErrorResponse Test")
@Slf4j
class ErrorResponseTest {

    /**
     * Current timestamp used for testing date-related operations.
     */
    private static LocalDateTime now;

    /**
     * HTTP status code representing a bad request error.
     */
    private static final int ERROR_CODE = 400;

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        now = LocalDateTime.now();
        log.info("Starting ErrorResponse tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished ErrorResponse tests");
    }

    /**
     * Verifies that the ErrorResponse builder correctly assigns all fields.
     */
    @Test
    @Order(1)
    @DisplayName("Should build ErrorResponse correctly")
    void testErrorResponseBuilder() {
        ErrorResponse.ValidationError fieldError = ErrorResponse.ValidationError.builder()
                .field("email")
                .rejectedValue("invalid-email")
                .message("Email must be valid")
                .build();

        ErrorResponse response = ErrorResponse.builder()
                .type("https://example.com/probs/invalid-request")
                .title("Invalid Request")
                .status(ERROR_CODE)
                .detail("Validation failed")
                .instance("/api/users")
                .timestamp(now)
                .errors(List.of(fieldError))
                .errorCode(ErrorCode.USER_NOT_FOUND)
                .build();

        assertAll("ErrorResponse fields",
                () -> assertEquals("https://example.com/probs/invalid-request", response.getType()),
                () -> assertEquals("Invalid Request", response.getTitle()),
                () -> assertEquals(ERROR_CODE, response.getStatus()),
                () -> assertEquals("Validation failed", response.getDetail()),
                () -> assertEquals("/api/users", response.getInstance()),
                () -> assertEquals(now, response.getTimestamp()),
                () -> assertNotNull(response.getErrors()),
                () -> assertEquals(1, response.getErrors().size()),
                () -> assertEquals(ErrorCode.USER_NOT_FOUND, response.getErrorCode())
        );

        ErrorResponse.ValidationError ve = response.getErrors().getFirst();
        assertAll("ValidationError fields",
                () -> assertEquals("email", ve.getField()),
                () -> assertEquals("invalid-email", ve.getRejectedValue()),
                () -> assertEquals("Email must be valid", ve.getMessage())
        );
    }

    /**
     * Verifies that ErrorResponse can be created with null or empty fields.
     */
    @Test
    @Order(2)
    @DisplayName("Should allow null or empty fields")
    void testNullFields() {
        ErrorResponse response = ErrorResponse.builder().build();
        assertAll("ErrorResponse null fields",
                () -> assertNull(response.getType()),
                () -> assertNull(response.getTitle()),
                () -> assertEquals(0, response.getStatus()),
                () -> assertNull(response.getDetail()),
                () -> assertNull(response.getInstance()),
                () -> assertNull(response.getTimestamp()),
                () -> assertNull(response.getErrors()),
                () -> assertNull(response.getErrorCode())
        );
    }
}
