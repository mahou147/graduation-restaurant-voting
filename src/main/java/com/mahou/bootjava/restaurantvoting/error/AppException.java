package com.mahou.bootjava.restaurantvoting.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppException extends RuntimeException {
    private final ErrorType type;
    private final String msgCode;
}