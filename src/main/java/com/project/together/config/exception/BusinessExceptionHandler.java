package com.project.together.config.exception;

import lombok.Getter;

/**
 * 에러를 사용하기 위한 구현체
 */
@Getter
public class BusinessExceptionHandler extends RuntimeException {

    private final int errorCode;

    public BusinessExceptionHandler(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
