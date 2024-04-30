package com.flab.fpay.domain.order.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
import com.flab.fpay.domain.order.enums.OrderStatus;
import jakarta.persistence.*;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "uidx_order_orderuniqueid", columnList = "order_unique_id", unique = true)
        }
)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "order_unique_id")
    private String orderUniqueId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "amount")
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    public Order() {
    }

    public Order(String orderUniqueId, long productId, int quantity, int amount, OrderStatus status) {
        this.orderUniqueId = orderUniqueId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private String orderUniqueId;
        private long productId;
        private int quantity;
        private int amount;
        private OrderStatus status;

        public OrderBuilder orderUniqueId(String orderUniqueId) {
            this.orderUniqueId = orderUniqueId;
            return this;
        }

        public OrderBuilder productId(long productId) {
            this.productId = productId;
            return this;
        }

        public OrderBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public OrderBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Order build() {
            return new Order(orderUniqueId, productId, quantity, amount, status);
        }
    }
}
