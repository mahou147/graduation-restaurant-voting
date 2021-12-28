package com.mahou.bootjava.restaurantvoting.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    APP_ERROR("error.appError", HttpStatus.INTERNAL_SERVER_ERROR),
    //  http://stackoverflow.com/a/22358422/548473
    DATA_NOT_FOUND("error.dataNotFound", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_ERROR("error.dataError", HttpStatus.CONFLICT),
    VALIDATION_ERROR("error.validationError", HttpStatus.UNPROCESSABLE_ENTITY),
    WRONG_REQUEST("error.wrongRequest", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final HttpStatus status;

    ErrorType(String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }
}
