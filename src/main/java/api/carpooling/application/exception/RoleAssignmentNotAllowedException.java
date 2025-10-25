package api.carpooling.application.exception;

import api.carpooling.application.exception.api.ApiException;
import api.carpooling.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the user has access forbidden
 * <p>
 * Typically raised during the demand access to update or enter in system
 * without authorization.
 */
public class RoleAssignmentNotAllowedException extends ApiException {

    /**
     * Creates a new API exception with a message.
     *
     * @param message    descriptive message for the error
     */
    public RoleAssignmentNotAllowedException(String message) {
        super(message, ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
    }
}
