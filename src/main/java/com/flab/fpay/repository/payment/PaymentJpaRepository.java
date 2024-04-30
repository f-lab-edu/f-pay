package com.flab.fpay.repository.payment;

import com.flab.fpay.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(long orderId);
}
