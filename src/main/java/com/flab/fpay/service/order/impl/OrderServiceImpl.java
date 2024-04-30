package com.flab.fpay.service.order.impl;

import com.flab.fpay.domain.order.dto.OrderDto;
import com.flab.fpay.domain.order.entity.Order;
import com.flab.fpay.domain.order.enums.OrderStatus;
import com.flab.fpay.repository.order.OrderJpaRepository;
import com.flab.fpay.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderJpaRepository;

    public OrderServiceImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public void createOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .orderUniqueId(orderDto.getOrderUniqueId())
                .productId(orderDto.getProductId())
                .quantity(orderDto.getQuantity())
                .amount(orderDto.getAmount())
                .status(orderDto.getStatus())
                .build();

        orderJpaRepository.save(order);
    }

    @Override
    public Order getOrderByUniqueId(String orderUniqueId) {
        return orderJpaRepository.findByOrderUniqueId(orderUniqueId);
    }

    /**
     * @param orderId : 검증된 주문 ID로 가정
     * 결제 & 주문 실패 관련 예외 발생 시 사용되므로 별도의 트랜잭션으로 동작한다.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFailedOrderStatus(long orderId) {
        Order order = orderJpaRepository.findById(orderId).get();
        order.setStatus(OrderStatus.FAILED);

        orderJpaRepository.save(order);
    }
}
