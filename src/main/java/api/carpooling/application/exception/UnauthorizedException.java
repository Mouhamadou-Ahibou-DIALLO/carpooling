package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when token invalid
 * that user uses when ask endpoint /me
 * <p>
 * Commonly used during when to see endpoint /me
 */
public class UnauthorizedException extends ApiException {

    /**
     * Creates a new API exception with a message.
     *
     * @param message    descriptive message for the error
     */
    public UnauthorizedException(String message) {
        super(message, ErrorCode.INVALID_TOKEN, HttpStatus.NOT_FOUND);
    }
}
