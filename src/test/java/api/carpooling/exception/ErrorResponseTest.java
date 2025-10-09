package api.carpooling.exception;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ErrorResponse} DTO.
 * <p>
 * These tests verify that the builder correctly assigns values,
 * that getters and setters work, and that the nested {@link ErrorResponse.ValidationError}
 * class behaves as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ErrorResponse Test")
class ErrorResponseTest {

    private static LocalDateTime now;

    @BeforeAll
    static void setUpAll() {
        now = LocalDateTime.now();
        System.out.println("Starting ErrorResponse tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished ErrorResponse tests");
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
                .status(400)
                .detail("Validation failed")
                .instance("/api/users")
                .timestamp(now)
                .errors(List.of(fieldError))
                .errorCode(ErrorCode.USER_NOT_FOUND)
                .build();

        assertAll("ErrorResponse fields",
                () -> assertEquals("https://example.com/probs/invalid-request", response.getType()),
                () -> assertEquals("Invalid Request", response.getTitle()),
                () -> assertEquals(400, response.getStatus()),
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
