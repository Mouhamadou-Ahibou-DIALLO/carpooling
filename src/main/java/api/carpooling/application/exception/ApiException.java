package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for all API-specific errors.
 * <p>
 * Each custom exception in the application should extend this class
 * to provide a consistent structure for error handling.
 * It associates a specific {@link ErrorCode} and {@link HttpStatus}
 * with each thrown exception.
 */
public abstract class ApiException extends RuntimeException {

    /**
     * Error code representing the type of application error.
     */
    private final ErrorCode errorCode;

    /**
     * HTTP status to return in the API response.
     */
    private final HttpStatus httpStatus;

    /**
     * Creates a new API exception with a message, error code, and HTTP status.
     *
     * @param message descriptive message for the error
     * @param errorCode application-specific error code
     * @param httpStatus corresponding HTTP status
     */
    protected ApiException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * Returns the associated error code.
     *
     * @return the {@link ErrorCode} linked to this exception
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the associated HTTP status.
     *
     * @return the {@link HttpStatus} linked to this exception
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
