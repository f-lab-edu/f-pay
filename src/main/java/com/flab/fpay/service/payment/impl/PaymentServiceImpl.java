package com.flab.fpay.service.payment.impl;

import com.flab.fpay.domain.payment.dto.PaymentDto;
import com.flab.fpay.domain.payment.entity.Payment;
import com.flab.fpay.repository.payment.PaymentJpaRepository;
import com.flab.fpay.service.payment.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentServiceImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public void createPayment(PaymentDto paymentDto) {
        Payment payment = Payment
                .builder()
                .orderId(paymentDto.getOrderId())
                .memberId(paymentDto.getMemberId())
                .amount(paymentDto.getAmount())
                .status(paymentDto.getPaymentStatus())
                .build();

        paymentJpaRepository.save(payment);
    }
}
