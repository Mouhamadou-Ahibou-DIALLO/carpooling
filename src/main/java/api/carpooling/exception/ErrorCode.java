package api.carpooling.exception;

/**
 * Enum representing custom error codes for API responses.
 */
public enum ErrorCode {
    AUTH_UNAUTHORIZED,
    VALIDATION_ERROR,
    INTERNAL_ERROR,
    USER_NOT_FOUND,
    USER_FOUND,
    AUTH_TOKEN_EXPIRED,
    AUTH_PASSWORD_INVALID,
}
