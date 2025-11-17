package ru.siganov.hot_wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST);


    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
