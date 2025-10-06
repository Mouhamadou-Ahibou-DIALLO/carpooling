package api.carpooling.utils;

import api.carpooling.exception.ErrorCode;
import api.carpooling.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseBuilder {
    private ErrorResponseBuilder() {}

    public static ErrorResponse build(HttpStatus status, String title,
                                      String detail, String instance, ErrorCode errorCode) {
        return ErrorResponse.builder()
                .type("about:blank")
                .title(title)
                .status(status.value())
                .detail(detail)
                .instance(instance)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .build();
    }
}

