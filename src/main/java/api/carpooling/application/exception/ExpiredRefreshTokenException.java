package api.carpooling.application.exception;

import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a refresh token is expired or no longer valid.
 * <p>
 * Typically used in authentication flows when a user attempts
 * to refresh an access token with an outdated refresh token.
 */
public class ExpiredRefreshTokenException extends ApiException {

    /**
     * Constructs a new {@code ExpiredRefreshTokenException} with a detailed message.
     *
     * @param message descriptive error message
     */
    public ExpiredRefreshTokenException(String message) {
        super(message, ErrorCode.AUTH_TOKEN_EXPIRED, HttpStatus.BAD_REQUEST);
    }
}
