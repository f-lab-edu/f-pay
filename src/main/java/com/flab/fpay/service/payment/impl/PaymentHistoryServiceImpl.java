package com.flab.fpay.service.payment.impl;

import com.flab.fpay.domain.payment.dto.PaymentHistoryDto;
import com.flab.fpay.domain.payment.entity.PaymentHistory;
import com.flab.fpay.repository.payment.PaymentHistoryJpaRepository;
import com.flab.fpay.service.payment.PaymentHistoryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PaymentHistoryJpaRepository paymentHistoryJpaRepository;

    public PaymentHistoryServiceImpl(PaymentHistoryJpaRepository paymentHistoryJpaRepository) {
        this.paymentHistoryJpaRepository = paymentHistoryJpaRepository;
    }

    @Override
    public void createPaymentHistory(PaymentHistoryDto paymentHistoryDto) {
        PaymentHistory paymentHistory = PaymentHistory
                .builder()
                .orderId(paymentHistoryDto.getOrderId())
                .memberId(paymentHistoryDto.getMemberId())
                .amount(paymentHistoryDto.getAmount())
                .status(paymentHistoryDto.getPaymentStatus())
                .build();

        paymentHistoryJpaRepository.save(paymentHistory);
    }

    @Override
    @Async("historyThreadPoolExecutor")
    public void createPaymentHistoryAsync(PaymentHistoryDto paymentHistoryDto) {
        PaymentHistory paymentHistory = PaymentHistory
                .builder()
                .orderId(paymentHistoryDto.getOrderId())
                .memberId(paymentHistoryDto.getMemberId())
                .amount(paymentHistoryDto.getAmount())
                .status(paymentHistoryDto.getPaymentStatus())
                .build();

        paymentHistoryJpaRepository.save(paymentHistory);
    }
}
