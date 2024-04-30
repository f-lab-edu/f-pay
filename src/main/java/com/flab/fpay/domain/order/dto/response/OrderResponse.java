package com.flab.fpay.domain.order.dto.response;

public class OrderResponse {
    private String orderUniqueId;

    public OrderResponse(String orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }

    public String getOrderUniqueId() {
        return orderUniqueId;
    }
}
