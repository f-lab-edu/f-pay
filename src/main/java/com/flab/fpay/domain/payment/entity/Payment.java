package com.flab.fpay.domain.payment.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
import com.flab.fpay.domain.payment.enums.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table(
        name = "payment"
)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "member_id")
    private long memberId;

    @Column(name = "amount")
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    protected Payment() {}

    private Payment(long orderId, long memberId, int amount, PaymentStatus status) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public static class PaymentBuilder {
        private long memberId;
        private long orderId;
        private int amount;
        private PaymentStatus status;
        private String data;

        private PaymentBuilder() {

        }

        public PaymentBuilder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public PaymentBuilder orderId(long orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public PaymentBuilder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public PaymentBuilder data(String data) {
            this.data = data;
            return this;
        }

        public Payment build() {
            return new Payment(memberId, orderId, amount, status);
        }
    }
}