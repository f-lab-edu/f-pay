package com.flab.fpay.domain.payment.dto;

import com.flab.fpay.domain.payment.enums.PaymentHistoryStatus;

public class PaymentHistoryDto {
    private final long orderId;
    private final long memberId;
    private final int amount;
    private final PaymentHistoryStatus status;
    private final String data;

    private PaymentHistoryDto(long orderId, long memberId, int amount, PaymentHistoryStatus status, String data) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
        this.data = data;
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

    public PaymentHistoryStatus getPaymentStatus() {
        return status;
    }

    public static PaymentHistoryDtoBuilder builder() {
        return new PaymentHistoryDtoBuilder();
    }

    public static class PaymentHistoryDtoBuilder {
        private long orderId;
        private long memberId;
        private int amount;
        private PaymentHistoryStatus paymentHistoryStatus;
        private String data;

        private PaymentHistoryDtoBuilder() {
        }

        public PaymentHistoryDtoBuilder orderId(long orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentHistoryDtoBuilder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public PaymentHistoryDtoBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public PaymentHistoryDtoBuilder paymentStatus(PaymentHistoryStatus paymentHistoryStatus) {
            this.paymentHistoryStatus = paymentHistoryStatus;
            return this;
        }

        public PaymentHistoryDtoBuilder data(String data) {
            this.data = data;
            return this;
        }

        public PaymentHistoryDto build() {
            return new PaymentHistoryDto(orderId, memberId, amount, paymentHistoryStatus, data);
        }
    }
}
