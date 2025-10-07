package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the provided password does not match
 * the stored password for a given user.
 * <p>
 * Typically raised during the login process to indicate
 * invalid authentication credentials.
 */
public class PasswordNotMatchException extends ApiException {

    /**
     * Constructs a new {@code PasswordNotMatchException} with a detailed message.
     *
     * @param message descriptive error message
     */
    public PasswordNotMatchException(String message) {
        super(message, ErrorCode.AUTH_PASSWORD_INVALID, HttpStatus.BAD_REQUEST);
    }
}
