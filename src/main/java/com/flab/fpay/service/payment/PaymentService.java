package com.flab.fpay.service.payment;

import com.flab.fpay.domain.payment.dto.PaymentDto;

public interface PaymentService {
    void createPayment(PaymentDto paymentDto);
}
