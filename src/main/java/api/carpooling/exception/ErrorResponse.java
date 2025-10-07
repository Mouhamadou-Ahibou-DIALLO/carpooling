package api.carpooling.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard structure for API error responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Type of error (RFC7807 standard). */
    private String type;

    /** Short title describing the error. */
    private String title;

    /** HTTP status code. */
    private int status;

    /** Detailed message explaining the error. */
    private String detail;

    /** Instance or URI of the request that caused the error. */
    private String instance;

    /** Timestamp when the error occurred. */
    private LocalDateTime timestamp;

    /** List of field validation errors. */
    private List<ValidationError> errors;

    /** Custom application-specific error code. */
    private ErrorCode errorCode;

    /**
     * Represents a field-level validation error.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {

        /** Name of the invalid field. */
        private String field;

        /** Value that was rejected. */
        private Object rejectedValue;

        /** Error message for this field. */
        private String message;
    }
}
