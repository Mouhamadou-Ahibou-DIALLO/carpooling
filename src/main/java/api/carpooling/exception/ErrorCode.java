package api.carpooling.exception;

/**
 * Enum representing custom error codes for API responses.
 */
public enum ErrorCode {

    /** Unauthorized access attempt, e.g., invalid credentials. */
    AUTH_UNAUTHORIZED,

    /** Input validation failed, e.g., missing or incorrect fields. */
    VALIDATION_ERROR,

    /** Internal server error occurred. */
    INTERNAL_ERROR,

    /** The requested user was not found in the system. */
    USER_NOT_FOUND,

    /** A user already exists when trying to create a new one. */
    USER_FOUND,

    /** JWT token has expired and is no longer valid. */
    AUTH_TOKEN_EXPIRED,

    /** Provided password is invalid. */
    AUTH_PASSWORD_INVALID,
}
