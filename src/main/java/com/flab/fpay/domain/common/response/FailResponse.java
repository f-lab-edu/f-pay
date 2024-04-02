package com.flab.fpay.domain.common.response;

public class FailResponse {
    private final String message;

    public FailResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
