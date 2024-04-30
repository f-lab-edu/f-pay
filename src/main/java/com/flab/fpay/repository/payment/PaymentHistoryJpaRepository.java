package com.flab.fpay.repository.payment;

import com.flab.fpay.domain.payment.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistory, Long> {
}
