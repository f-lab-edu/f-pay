package com.flab.fpay.domain.payment.entity;

import com.flab.fpay.domain.payment.enums.PaymentHistoryStatus;
import jakarta.persistence.*;

@Entity
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "member_id")
    private long memberId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "amount")
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentHistoryStatus status;

    @Column(name = "data")
    private String data;

    protected PaymentHistory() {
    }

    private PaymentHistory(long memberId, long orderId, int amount, PaymentHistoryStatus status, String data) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.data = data;
    }

    public static PaymentHistoryBuilder builder() {
        return new PaymentHistoryBuilder();
    }

    public static class PaymentHistoryBuilder {
        private long memberId;
        private long orderId;
        private int amount;
        private PaymentHistoryStatus status;
        private String data;

        private PaymentHistoryBuilder() {

        }

        public PaymentHistoryBuilder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public PaymentHistoryBuilder orderId(long orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentHistoryBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public PaymentHistoryBuilder status(PaymentHistoryStatus status) {
            this.status = status;
            return this;
        }

        public PaymentHistoryBuilder data(String data) {
            this.data = data;
            return this;
        }

        public PaymentHistory build() {
            return new PaymentHistory(memberId, orderId, amount, status, data);
        }
    }
}
