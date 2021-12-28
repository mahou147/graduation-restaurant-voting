package com.mahou.bootjava.restaurantvoting.error;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorType type;
    private final String msgCode;

    public AppException(String msgCode, ErrorType type) {
        this.msgCode = msgCode;
        this.type = type;
    }
}