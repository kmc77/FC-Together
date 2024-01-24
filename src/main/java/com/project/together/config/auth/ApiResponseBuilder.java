package com.project.together.config.auth;

public class ApiResponseBuilder {
    private String result;
    private int resultCode;
    private String resultMsg;

    public ApiResponseBuilder result(String result) {
        this.result = result;
        return this;
    }

    public ApiResponseBuilder resultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public ApiResponseBuilder resultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
        return this;
    }

    public ApiResponse build() {
        return new ApiResponse(result, resultCode, resultMsg);
    }
}
