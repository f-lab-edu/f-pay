package com.flab.fpay.facade.order;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.order.dto.OrderDto;
import com.flab.fpay.domain.order.dto.request.OrderRequest;
import com.flab.fpay.domain.order.dto.response.OrderResponse;
import com.flab.fpay.domain.order.enums.OrderStatus;
import com.flab.fpay.domain.product.entity.Product;
import com.flab.fpay.domain.product.enums.ProductStatus;
import com.flab.fpay.domain.vendor.entity.Vendor;
import com.flab.fpay.domain.vendor.enums.VendorStatus;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.service.member.MemberService;
import com.flab.fpay.service.order.OrderService;
import com.flab.fpay.service.product.ProductService;
import com.flab.fpay.service.vendor.VendorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderFacadeService {
    private final MemberService memberService;
    private final ProductService productService;
    private final VendorService vendorService;
    private final OrderService orderService;

    public OrderFacadeService(MemberService memberService, ProductService productService, VendorService vendorService, OrderService orderService) {
        this.memberService = memberService;
        this.productService = productService;
        this.vendorService = vendorService;
        this.orderService = orderService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderResponse registerOrder(OrderRequest orderRequest, long memberId) {
        // 회원 검증
        Member member = memberService.getMemberById(memberId);

        if(member == null) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 상품 검증
        Product product = productService.getProductById(orderRequest.getProductId());

        if(product == null || product.getStatus() != ProductStatus.ON_SALE) {
            throw new ApiException(ErrorCode.INVALID_PRODUCT);
        }

        // 가맹점 검증
        Vendor vendor = vendorService.getVendorById(product.getVendorId());

        if(vendor == null || vendor.getStatus() != VendorStatus.REGISTERED) {
            throw new ApiException(ErrorCode.INVALID_VENDOR);
        }

        // 재고 검증
        if(product.getQuantity() < orderRequest.getQuantity()) {
            throw new ApiException(ErrorCode.OUT_OF_STOCK);
        }

        // 재고 차감
        productService.decreaseQuantity(product.getId());

        // 총 주문 금액
        int amount = productService.getTotalPrice(product, orderRequest.getQuantity());

        String orderUniqueId = UUID.randomUUID().toString();

        // 주문 테이블에 주문 진행중 상태 데이터 생성
        OrderDto orderDto = OrderDto.builder()
                .orderUniqueId(orderUniqueId)
                .productId(product.getId())
                .amount(amount)
                .quantity(orderRequest.getQuantity())
                .status(OrderStatus.PROCEEDING)
                .build();

        orderService.createOrder(orderDto);

        return new OrderResponse(orderUniqueId);
    }
}
