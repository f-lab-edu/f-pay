package com.flab.fpay.integration.payment.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.fpay.domain.auth.constant.AuthConstant;
import com.flab.fpay.domain.auth.dto.JwtClaimDto;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.order.entity.Order;
import com.flab.fpay.domain.order.enums.OrderStatus;
import com.flab.fpay.domain.payment.dto.request.PaymentRequest;
import com.flab.fpay.domain.payment.entity.Payment;
import com.flab.fpay.domain.payment.enums.PaymentStatus;
import com.flab.fpay.domain.product.entity.Product;
import com.flab.fpay.domain.product.enums.ProductCategory;
import com.flab.fpay.domain.product.enums.ProductStatus;
import com.flab.fpay.integration.IntegrationTest;
import com.flab.fpay.integration.dto.RandomLoginMemberDto;
import com.flab.fpay.repository.member.MemberJpaRepository;
import com.flab.fpay.repository.order.OrderJpaRepository;
import com.flab.fpay.repository.payment.PaymentJpaRepository;
import com.flab.fpay.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentIntegrationV1Test extends IntegrationTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OrderJpaRepository orderJpaRepository;
    @Autowired
    ProductJpaRepository productJpaRepository;
    @Autowired
    PaymentJpaRepository paymentJpaRepository;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("결제 성공")
    public void requestPaymentSuccess() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();
        Product product = this.createProduct(1L, "상품", "일렬번호", 10_000, 100, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        String orderUniqueId = "orderUniqueId";
        Order order = this.createOrder(orderUniqueId, product.getId(), 1, 10_000, OrderStatus.PROCEEDING);

        PaymentRequest paymentRequest = new PaymentRequest(orderUniqueId);

        // when & then
        mockMvc.perform(post("/v1/payments/pay")
                        .content(objectMapper.writeValueAsString(paymentRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status()
                        .isOk());

        Member member = memberJpaRepository.findById(randomLoginMemberDto.getMemberId()).get();
        Payment payment = paymentJpaRepository.findByOrderId(order.getId());

        assertThat(member.getBalance()).isEqualTo(memberBalance - order.getAmount());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETE);
    }

    @Test
    @DisplayName("회원 검증 실패")
    public void memberNotFound() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();
        Product product = this.createProduct(1L, "상품", "일렬번호", 10_000, 100, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        String orderUniqueId = "orderUniqueId";
        this.createOrder(orderUniqueId, product.getId(), 1, 10_000, OrderStatus.PROCEEDING);

        long invalidMemberId = randomLoginMemberDto.getMemberId() + 1;

        JwtClaimDto jwtClaimDto = JwtClaimDto.builder()
                .memberId(invalidMemberId)
                .deviceId("deviceId")
                .issuedAt(new Date())
                .expiredAt(getExpiredAt(new Date(), 1))
                .build();

        String invalidMemberToken = this.createToken(jwtClaimDto);

        PaymentRequest paymentRequest = new PaymentRequest(orderUniqueId);

        // when & then
        mockMvc.perform(post("/v1/payments/pay")
                        .content(objectMapper.writeValueAsString(paymentRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + invalidMemberToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.MEMBER_NOT_FOUND.getMessage())))
                        .andExpect(status()
                        .isNotFound());

    }

    @Test
    @DisplayName("주문 검증 실패")
    public void invalidOrder() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();
        Product product = this.createProduct(1L, "상품", "일렬번호", 10_000, 100, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        String orderUniqueId = "orderUniqueId";
        this.createOrder(orderUniqueId, product.getId(), 1, 10_000, OrderStatus.COMPLETED);

        PaymentRequest paymentRequest = new PaymentRequest(orderUniqueId);

        // when & then
        mockMvc.perform(post("/v1/payments/pay")
                        .content(objectMapper.writeValueAsString(paymentRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_ORDER.getMessage())))
                        .andExpect(status()
                        .isBadRequest());

    }
}
