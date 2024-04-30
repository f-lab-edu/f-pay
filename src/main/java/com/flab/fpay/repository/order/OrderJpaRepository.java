package com.flab.fpay.repository.order;

import com.flab.fpay.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    Order findByOrderUniqueId(String orderUniqueId);
}
