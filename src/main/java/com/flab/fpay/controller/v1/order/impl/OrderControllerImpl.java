package com.flab.fpay.controller.v1.order.impl;

import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import com.flab.fpay.domain.order.dto.request.OrderRequest;
import com.flab.fpay.domain.order.dto.response.OrderResponse;
import com.flab.fpay.facade.order.OrderFacadeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.flab.fpay.domain.auth.constant.AuthConstant.MEMBER_ID_CLAIM_KEY;

@RestController
public class OrderControllerImpl {

    private final OrderFacadeService orderFacadeService;

    public OrderControllerImpl(OrderFacadeService orderFacadeService) {
        this.orderFacadeService = orderFacadeService;
    }

    @PostMapping("/v1/orders")
    public ResponseEntity<SuccessResponse> registerOrder(@RequestBody @Valid OrderRequest orderRequest,
                                                         HttpServletRequest httpServletRequest) {
        long memberId = Long.parseLong(httpServletRequest.getAttribute(MEMBER_ID_CLAIM_KEY).toString());
        OrderResponse orderResponse = orderFacadeService.registerOrder(orderRequest, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(orderResponse));
    }
}
