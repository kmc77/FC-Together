package com.project.together.common.response;

import lombok.Data;

@Data
public class ApiResponse {
    private String result;
    private int resultCode;
    private String resultMsg;

    public ApiResponse() {
    }

    public ApiResponse(String result, int resultCode, String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static ApiResponseBuilder builder() {
        return new ApiResponseBuilder();
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
