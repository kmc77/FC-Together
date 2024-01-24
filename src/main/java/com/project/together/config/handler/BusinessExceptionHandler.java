package com.project.together.config.handler;

public class BusinessExceptionHandler extends RuntimeException {

    private final int errorCode;

    public BusinessExceptionHandler(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
