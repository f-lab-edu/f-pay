package com.flab.fpay.facade.payment;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.order.entity.Order;
import com.flab.fpay.domain.order.enums.OrderStatus;
import com.flab.fpay.domain.payment.dto.PaymentDto;
import com.flab.fpay.domain.payment.dto.PaymentHistoryDto;
import com.flab.fpay.domain.payment.dto.request.PaymentRequest;
import com.flab.fpay.domain.payment.enums.PaymentHistoryStatus;
import com.flab.fpay.domain.payment.enums.PaymentStatus;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.exception.BankException;
import com.flab.fpay.service.bank.BankService;
import com.flab.fpay.service.member.MemberService;
import com.flab.fpay.service.order.OrderService;
import com.flab.fpay.service.payment.PaymentHistoryService;
import com.flab.fpay.service.payment.PaymentService;
import com.flab.fpay.service.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentFacadeService {
    private final MemberService memberService;
    private final PaymentHistoryService paymentHistoryService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final BankService bankService;
    private final ProductService productService;

    public PaymentFacadeService(MemberService memberService,
                                PaymentHistoryService paymentHistoryService,
                                PaymentService paymentService,
                                OrderService orderService,
                                BankService bankService, ProductService productService) {
        this.memberService = memberService;
        this.paymentHistoryService = paymentHistoryService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.bankService = bankService;
        this.productService = productService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requestPayment(PaymentRequest paymentRequest, long memberId) {
        // 회원 검증
        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 주문 검증
        Order order = orderService.getOrderByUniqueId(paymentRequest.getOrderUniqueId());

        if (order == null || order.getStatus() != OrderStatus.PROCEEDING) {
            throw new ApiException(ErrorCode.INVALID_ORDER);
        }

        // 결제 요청 내역 기록 (비동기)
        PaymentHistoryDto requestPaymentHistoryDto = PaymentHistoryDto.builder()
                .orderId(order.getId())
                .memberId(memberId)
                .amount(order.getAmount())
                .paymentStatus(PaymentHistoryStatus.PAYMENT_REQUESTED)
                .data(null)
                .build();

        paymentHistoryService.createPaymentHistoryAsync(requestPaymentHistoryDto);

        try {
            // 잔액 검증, 잔액 부족 시 은행 서비스를 사용한 충전 요청
            if (member.getBalance() < order.getAmount()) {
                bankService.chargePaymentMoney(member.getId(), order.getAmount());
            }

            // 회원 페이머니 차감
            memberService.decreasePaymoney(member.getId(), order.getAmount());

            // 결제 성공 내역 기록
            PaymentHistoryDto completePaymentHistoryDto = PaymentHistoryDto.builder()
                    .orderId(order.getId())
                    .memberId(memberId)
                    .amount(order.getAmount())
                    .paymentStatus(PaymentHistoryStatus.PAYMENT_COMPLETE)
                    .data(null)
                    .build();

            paymentHistoryService.createPaymentHistory(completePaymentHistoryDto);

            // 결제 최종 결과 기록
            PaymentDto paymentDto = PaymentDto.builder()
                    .orderId(order.getId())
                    .memberId(memberId)
                    .amount(order.getAmount())
                    .paymentStatus(PaymentStatus.COMPLETE)
                    .build();

            paymentService.createPayment(paymentDto);

            // 주문 상태를 주문 완료로 업데이트
            order.setStatus(OrderStatus.COMPLETED);

        } catch (BankException bankException) {
            // 결제 내역(은행 서비스 오류) 기록
            this.createFailedPaymentHistory(order.getId(), memberId, order.getAmount(), PaymentHistoryStatus.BANK_SERVICE_FAILED);

            this.handleFailedPayment(order.getId(), order.getProductId());

            throw new ApiException(ErrorCode.BANK_SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            // 결제 내역(실패) 기록
            this.createFailedPaymentHistory(order.getId(), memberId, order.getAmount(), PaymentHistoryStatus.PAYMENT_FAILED);

            this.handleFailedPayment(order.getId(), order.getProductId());

            throw new ApiException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private void createFailedPaymentHistory(long orderId, long memberId, int amount, PaymentHistoryStatus status) {
        PaymentHistoryDto failedPaymentHistoryDto = PaymentHistoryDto.builder()
                .orderId(orderId)
                .memberId(memberId)
                .amount(amount)
                .paymentStatus(status)
                .data(null)
                .build();

        paymentHistoryService.createPaymentHistoryAsync(failedPaymentHistoryDto);
    }

    private void handleFailedPayment(long orderId, long productId) {
        // 주문 상태를 주문 실패로 업데이트
        orderService.updateFailedOrderStatus(orderId);
        // 상품 재고 원복
        productService.increaseQuantity(productId);
    }
}

