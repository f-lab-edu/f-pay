package com.flab.fpay.domain.order.dto;

import com.flab.fpay.domain.order.enums.OrderStatus;

public class OrderDto {
    private final String orderUniqueId;
    private final long productId;
    private final int quantity;
    private final int amount;
    private final OrderStatus status;

    private OrderDto(String orderUniqueId, long productId, int quantity, int amount, OrderStatus status) {
        this.orderUniqueId = orderUniqueId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }

    public String getOrderUniqueId() {
        return orderUniqueId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public static OrderDtoBuilder builder() {
        return new OrderDtoBuilder();
    }

    public static class OrderDtoBuilder {
        private String orderUniqueId;
        private long productId;
        private int quantity;
        private int amount;
        private OrderStatus status;

        private OrderDtoBuilder () {}

        public OrderDtoBuilder orderUniqueId(String orderUniqueId) {
            this.orderUniqueId = orderUniqueId;
            return this;
        }

        public OrderDtoBuilder productId(long productId) {
            this.productId = productId;
            return this;
        }

        public OrderDtoBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderDtoBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public OrderDtoBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderDto build() {
            return new OrderDto(orderUniqueId, productId, quantity, amount, status);
        }

    }
}
