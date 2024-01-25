package com.project.together.common.codes;

public class SuccessCode {
    public static final Success SELECT = new Success(200, "Success");

    public static class Success {
        private final int status;
        private final String message;

        public Success(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
