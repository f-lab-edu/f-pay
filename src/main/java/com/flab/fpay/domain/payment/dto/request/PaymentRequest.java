package com.flab.fpay.domain.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
    @NotNull
    private String orderUniqueId;

    public String getOrderUniqueId() {
        return orderUniqueId;
    }

    public PaymentRequest() {}

    public PaymentRequest(String orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }
}

