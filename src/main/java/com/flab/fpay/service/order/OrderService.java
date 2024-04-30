package com.flab.fpay.service.order;

import com.flab.fpay.domain.order.dto.OrderDto;
import com.flab.fpay.domain.order.entity.Order;

public interface OrderService {
    void createOrder(OrderDto orderDto);

    Order getOrderByUniqueId(String orderUniqueId);

    void updateFailedOrderStatus(long orderId);
}
