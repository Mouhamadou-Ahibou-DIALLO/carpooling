package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to update in a user
 * that not verified in the system.
 * <p>
 * Commonly used during updating processes
 * to enforce unique constraints on user data.
 */
public class UserNotVerifiedException extends ApiException {
    /**
     * Creates a new API exception with a message, error code, and HTTP status.
     *
     * @param message    descriptive message for the error
     */
    protected UserNotVerifiedException(String message) {
        super(message, ErrorCode.USER_NOT_VERIFIED, HttpStatus.UNAUTHORIZED);
    }
}
