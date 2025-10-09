package api.carpooling.exception.handler;

import api.carpooling.application.exception.*;
import api.carpooling.exception.ErrorCode;
import api.carpooling.exception.ErrorResponse;
import api.carpooling.utils.ErrorResponseBuilder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the Carpooling API.
 * Catches specific and generic exceptions and returns standardized error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors thrown by @Valid annotations.
     *
     * @param ex the validation exception
     * @param request the HTTP request
     * @return ResponseEntity with ErrorResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Invalid request data")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.VALIDATION_ERROR)
                .errors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles generic exceptions not explicitly handled elsewhere.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return ResponseEntity with ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Something went wrong, please try again later",
                request.getRequestURI(),
                ErrorCode.INTERNAL_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Maps a FieldError to ValidationError.
     *
     * @param fieldError the field error
     * @return ValidationError object
     */
    private ErrorResponse.ValidationError mapFieldError(FieldError fieldError) {
        return ErrorResponse.ValidationError.builder()
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    /**
     * Handles all custom {@link ApiException} types thrown within the application.
     * <p>
     * This method centralizes the processing of domain-specific exceptions
     * by converting them into a structured {@link ErrorResponse} object,
     * ensuring consistent error formatting across the entire API.
     * <p>
     * The returned response includes details such as HTTP status, error code,
     * descriptive message, and the URI where the exception occurred.
     *
     * @param ex the custom {@code ApiException} thrown during request processing
     * @param request the current HTTP request from which the error originated
     * @return a {@link ResponseEntity} containing the formatted {@link ErrorResponse}
     *         and the appropriate HTTP status code defined in the exception
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                ex.getHttpStatus(),
                ex.getMessage(),
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
}
