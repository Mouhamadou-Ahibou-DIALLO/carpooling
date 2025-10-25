package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to log in a user
 * that not active in the system.
 * <p>
 * Commonly used during login processes
 * to enforce unique constraints on user data.
 */
public class UserNotActiveException extends ApiException {

    /**
     * Creates a new API exception with a message, error code, and HTTP status.
     *
     * @param message    descriptive message for the error
     */
    public UserNotActiveException(String message) {
        super(message, ErrorCode.USER_NOT_ACTIVE, HttpStatus.UNAUTHORIZED);
    }
}
