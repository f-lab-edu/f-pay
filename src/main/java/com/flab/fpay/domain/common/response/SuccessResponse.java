package com.flab.fpay.domain.common.response;

public class SuccessResponse<T> {
    private final T payload;

    public SuccessResponse(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
