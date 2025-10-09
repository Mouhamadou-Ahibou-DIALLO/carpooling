package api.carpooling.utils;

import api.carpooling.exception.ErrorCode;
import api.carpooling.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Utility class to build standardized ErrorResponse objects.
 */
public class ErrorResponseBuilder {

    /**
     * Private constructor to prevent instantiation.
     */
    private ErrorResponseBuilder() { }

    /**
     * Builds an ErrorResponse with the given parameters.
     *
     * @param status HTTP status
     * @param title short title describing the error
     * @param detail detailed error message
     * @param instance unique identifier for the error occurrence
     * @param errorCode custom error code
     * @return ErrorResponse object
     */
    public static ErrorResponse build(HttpStatus status, String title,
                                      String detail, String instance, ErrorCode errorCode) {
        return ErrorResponse.builder()
                .type("about:blank")
                .title(title)
                .status(status.value())
                .detail(detail)
                .instance(instance)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .build();
    }
}
