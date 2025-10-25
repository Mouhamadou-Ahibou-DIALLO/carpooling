package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to create a user
 * that already exists in the system.
 * <p>
 * Commonly used during registration or update processes
 * to enforce unique constraints on user data.
 */
public class UserExistsAlready extends ApiException {

    /**
     * Constructs a new {@code UserExistsAlready} with a detailed message.
     *
     * @param message descriptive error message
     */
    public UserExistsAlready(String message) {
        super(message, ErrorCode.USER_FOUND, HttpStatus.FOUND);
    }
}
