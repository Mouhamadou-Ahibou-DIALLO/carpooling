package api.carpooling.exception.handler;

import api.carpooling.application.exception.UserNotFoundException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * These tests verify that the exception handler returns
 * properly formatted {@link ErrorResponse} objects for
 * different exception types, including validation errors,
 * custom API exceptions, and generic exceptions.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("GlobalExceptionHandler Test")
@Slf4j
class GlobalExceptionHandlerTest {

    /**
     * Global exception handler used to manage and test API error responses.
     */
    private static GlobalExceptionHandler handler;

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        handler = new GlobalExceptionHandler();
        log.info("Starting GlobalExceptionHandler tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished GlobalExceptionHandler tests");
    }

    /**
     * Verifies that MethodArgumentNotValidException is correctly handled.
     */
    @Test
    @Order(1)
    @DisplayName("Should handle MethodArgumentNotValidException correctly")
    void testHandleValidation() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");

        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field1", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorCode.VALIDATION_ERROR, response.getBody().getErrorCode());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("field1", response.getBody().getErrors().getFirst().getField());
    }

    /**
     * Verifies that generic exceptions are correctly handled.
     */
    @Test
    @Order(2)
    @DisplayName("Should handle generic Exception correctly")
    void testHandleGeneric() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");

        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorCode.INTERNAL_ERROR, response.getBody().getErrorCode());
        assertEquals("Something went wrong, please try again later", response.getBody().getDetail());
    }

    /**
     * Verifies that custom ApiException is correctly handled.
     */
    @Test
    @Order(3)
    @DisplayName("Should handle ApiException correctly")
    void testHandleApiException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");

        UserNotFoundException ex = new UserNotFoundException("User missing");

        ResponseEntity<ErrorResponse> response = handler.handleApiException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorCode.USER_NOT_FOUND, response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getInstance());
    }
}
