package api.carpooling.utils;

import api.carpooling.exception.ErrorCode;
import api.carpooling.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;

import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link ErrorResponseBuilder}.
 * <p>
 * Verifies that ErrorResponse objects are correctly built with the given parameters.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ErrorResponseBuilder Test")
@Slf4j
class ErrorResponseBuilderTest {

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting ErrorResponseBuilder tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished ErrorResponseBuilder tests");
    }

    /**
     * Verifies that an ErrorResponse is correctly built.
     */
    @Test
    @Order(1)
    @DisplayName("Should build ErrorResponse correctly")
    void testBuildErrorResponse() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String title = "Validation Failed";
        String detail = "Invalid request data";
        String instance = "/api/test";
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        ErrorResponse response = ErrorResponseBuilder.build(status, title, detail, instance, errorCode);

        assertNotNull(response, "ErrorResponse should not be null");
        assertEquals("about:blank", response.getType(), "Type should be 'about:blank'");
        assertEquals(title, response.getTitle(), "Title should match");
        assertEquals(status.value(), response.getStatus(), "Status should match");
        assertEquals(detail, response.getDetail(), "Detail should match");
        assertEquals(instance, response.getInstance(), "Instance should match");
        assertEquals(errorCode, response.getErrorCode(), "ErrorCode should match");
        assertNotNull(response.getTimestamp(), "Timestamp should not be null");
    }
}
