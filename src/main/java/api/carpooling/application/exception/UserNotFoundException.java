package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user cannot be found in the system.
 * <p>
 * Typically raised during authentication or data retrieval operations
 * when the requested user does not exist in the database.
 */
public class UserNotFoundException extends ApiException {

    /**
     * Constructs a new {@code UserNotFoundException} with a detailed message.
     *
     * @param message descriptive error message
     */
    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
