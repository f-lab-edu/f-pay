package com.flab.fpay.domain.payment.dto;

import com.flab.fpay.domain.payment.enums.PaymentStatus;

public class PaymentDto {
    private final long orderId;
    private final long memberId;
    private final int amount;
    private final PaymentStatus status;

    private PaymentDto(long orderId, long memberId, int amount, PaymentStatus status) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return status;
    }

    public static PaymentDtoBuilder builder() {
        return new PaymentDtoBuilder();
    }

    public static class PaymentDtoBuilder {
        private long orderId;
        private long memberId;
        private int amount;
        private PaymentStatus status;

        private PaymentDtoBuilder() {
        }

        public PaymentDtoBuilder orderId(long orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentDtoBuilder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public PaymentDtoBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public PaymentDtoBuilder paymentStatus(PaymentStatus paymentHistoryStatus) {
            this.status = paymentHistoryStatus;
            return this;
        }

        public PaymentDto build() {
            return new PaymentDto(orderId, memberId, amount, status);
        }
    }
}
