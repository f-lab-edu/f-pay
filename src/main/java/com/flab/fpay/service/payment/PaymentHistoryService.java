package com.flab.fpay.service.payment;

import com.flab.fpay.domain.payment.dto.PaymentHistoryDto;

public interface PaymentHistoryService {
    void createPaymentHistory(PaymentHistoryDto paymentHistoryDto);
    void createPaymentHistoryAsync(PaymentHistoryDto paymentHistoryDto);
}
