package ru.zrv.newspagespr.newspage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

    private final HttpStatus httpStatus;

    private AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static AppException of(ErrorType errorType, Object... args) {
        return new AppException(String.format(errorType.getMessage(), args), errorType.getHttpStatus());
    }

    public static AppException of(ErrorType errorType) {
        return new AppException(errorType.getMessage(), errorType.getHttpStatus());
    }

}
