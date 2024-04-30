package com.flab.fpay.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull
    private long productId;

    @NotNull
    private int quantity;

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderRequest(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
