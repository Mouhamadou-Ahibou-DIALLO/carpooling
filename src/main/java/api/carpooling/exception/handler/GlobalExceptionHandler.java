package api.carpooling.exception.handler;

import api.carpooling.application.exception.ExpiredRefreshTokenException;
import api.carpooling.application.exception.PasswordNotMatchException;
import api.carpooling.application.exception.UserExistsAlready;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.exception.ErrorCode;
import api.carpooling.exception.ErrorResponse;
import api.carpooling.utils.ErrorResponseBuilder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation error: {}", ex.getMessage());

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Invalid request data")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.VALIDATION_ERROR)
                .errors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Something went wrong, please try again later",
                request.getRequestURI(),
                ErrorCode.INTERNAL_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ErrorResponse.ValidationError mapFieldError(FieldError fieldError) {
        return ErrorResponse.ValidationError.builder()
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex,
                                                            HttpServletRequest request) {
        log.error("User not found", ex);
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(),
                request.getRequestURI(), ErrorCode.USER_NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserExistsAlready.class)
    public ResponseEntity<ErrorResponse> handleUserExistAlready(UserExistsAlready ex,
                                                                HttpServletRequest request) {
        log.error("User already exists", ex);
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                HttpStatus.FOUND, "Found User", ex.getMessage(),
                request.getRequestURI(), ErrorCode.USER_FOUND);

        return ResponseEntity.status(HttpStatus.FOUND).body(errorResponse);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredRefreshToken(ExpiredRefreshTokenException ex,
                                                                   HttpServletRequest request) {
        log.error("Expired refresh token", ex);
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                HttpStatus.BAD_REQUEST, "Expired Token Refresh",
                ex.getMessage(), request.getRequestURI(),
                ErrorCode.AUTH_TOKEN_EXPIRED);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordNotMatch(PasswordNotMatchException ex,
                                                                HttpServletRequest request) {
        log.error("Password not match", ex);
        ErrorResponse errorResponse = ErrorResponseBuilder.build(
                HttpStatus.BAD_REQUEST, "Password invalid", ex.getMessage(),
                request.getRequestURI(), ErrorCode.AUTH_PASSWORD_INVALID);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
