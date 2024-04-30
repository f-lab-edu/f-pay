package com.flab.fpay.integration.order.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.fpay.domain.auth.constant.AuthConstant;
import com.flab.fpay.domain.auth.dto.JwtClaimDto;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.order.dto.request.OrderRequest;
import com.flab.fpay.domain.order.entity.Order;
import com.flab.fpay.domain.order.enums.OrderStatus;
import com.flab.fpay.domain.product.entity.Product;
import com.flab.fpay.domain.product.enums.ProductCategory;
import com.flab.fpay.domain.product.enums.ProductStatus;
import com.flab.fpay.domain.vendor.entity.Vendor;
import com.flab.fpay.domain.vendor.enums.VendorStatus;
import com.flab.fpay.integration.IntegrationTest;
import com.flab.fpay.integration.dto.RandomLoginMemberDto;
import com.flab.fpay.repository.order.OrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderIntegrationV1Test extends IntegrationTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OrderJpaRepository orderJpaRepository;
    @Test
    @DisplayName("주문 등록 성공")
    public void registerOrderSuccess() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.REGISTERED);

        int productQuantity = 10;
        Product product = this.createProduct(vendor.getId(), "상품", "일렬번호", 10_000, productQuantity, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        int orderQuantity = 1;
        OrderRequest orderRequest = new OrderRequest(product.getId(), orderQuantity);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.payload.orderUniqueId", is(notNullValue())))
                        .andExpect(status()
                        .isOk());

        Order savedOrder = orderJpaRepository.findAll().getFirst();

        assertThat(savedOrder.getProductId()).isEqualTo(product.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PROCEEDING);
        assertThat(savedOrder.getQuantity()).isEqualTo(orderQuantity);
        assertThat(savedOrder.getAmount()).isEqualTo(orderQuantity * product.getPrice());

        assertThat(product.getQuantity()).isEqualTo(productQuantity - 1);
    }

    @Test
    @DisplayName("회원 검증 실패")
    public void memberNotFound() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.REGISTERED);

        int productQuantity = 10;
        Product product = this.createProduct(vendor.getId(), "상품", "일렬번호", 10_000, productQuantity, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        int orderQuantity = 1;
        OrderRequest orderRequest = new OrderRequest(product.getId(), orderQuantity);

        long invalidMemberId = randomLoginMemberDto.getMemberId() + 1;

        JwtClaimDto jwtClaimDto = JwtClaimDto.builder()
                .memberId(invalidMemberId)
                .deviceId("deviceId")
                .issuedAt(new Date())
                .expiredAt(getExpiredAt(new Date(), 1))
                .build();

        String invalidMemberToken = this.createToken(jwtClaimDto);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + invalidMemberToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.MEMBER_NOT_FOUND.getMessage())))
                        .andExpect(status()
                        .isNotFound());

        assertThat(product.getQuantity()).isEqualTo(productQuantity);
    }

    @Test
    @DisplayName("상품 ID 검증 실패")
    public void productNotFound() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.REGISTERED);

        int productQuantity = 10;
        Product product = this.createProduct(vendor.getId(), "상품", "일렬번호", 10_000, productQuantity, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        int orderQuantity = 1;

        long invalidProductId = product.getId() + 1;
        OrderRequest orderRequest = new OrderRequest(invalidProductId, orderQuantity);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_PRODUCT.getMessage())))
                        .andExpect(status()
                        .isBadRequest());

        assertThat(product.getQuantity()).isEqualTo(productQuantity);
    }

    @Test
    @DisplayName("상품 상태 검증 실패")
    public void productStatusIsNotOnSale() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.REGISTERED);

        Product product = this.createProduct(vendor.getId(), "상품", "일렬번호", 10_000, 10, ProductCategory.TICKET, ProductStatus.SUSPENDED, "imageUrl");

        int orderQuantity = 1;
        OrderRequest orderRequest = new OrderRequest(product.getId(), orderQuantity);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_PRODUCT.getMessage())))
                        .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DisplayName("상품 재고 부족")
    public void productOutOfStock() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.REGISTERED);

        int productQuantity = 10;
        Product product = this.createProduct(vendor.getId(), "상품", "일렬번호", 10_000, productQuantity, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        int invalidQuantity = product.getQuantity() + 1000;
        OrderRequest orderRequest = new OrderRequest(product.getId(), invalidQuantity);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.OUT_OF_STOCK.getMessage())))
                        .andExpect(status()
                        .isBadRequest());

        assertThat(product.getQuantity()).isEqualTo(productQuantity);
    }

    @Test
    @DisplayName("가맹점 ID 검증 실패")
    public void vendorNotFound() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.REGISTERED);

        int productQuantity = 10;
        long invalidVendorId = vendor.getId() + 1;
        Product product = this.createProduct(invalidVendorId, "상품", "일렬번호", 10_000, productQuantity, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        int orderQuantity = 1;
        OrderRequest orderRequest = new OrderRequest(product.getId(), orderQuantity);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_VENDOR.getMessage())))
                        .andExpect(status()
                        .isBadRequest());

        assertThat(product.getQuantity()).isEqualTo(productQuantity);
    }

    @Test
    @DisplayName("가맹점 상태 검증 실패")
    public void vendorStatusIsNotRegistered() throws Exception {
        // given
        RandomLoginMemberDto randomLoginMemberDto = this.loginRandomMember();

        Vendor vendor = this.createVendor(randomLoginMemberDto.getMemberId(), "사업자등록", "가맹점", "김대표", "01022221111", VendorStatus.SUSPENDED);

        int productQuantity = 10;
        Product product = this.createProduct(vendor.getId(), "상품", "일렬번호", 10_000, productQuantity, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl");

        int orderQuantity = 1;
        OrderRequest orderRequest = new OrderRequest(product.getId(), orderQuantity);

        // when & then
        mockMvc.perform(post("/v1/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .header(AuthConstant.AUTHORIZATION_HEADER, AuthConstant.BEARER_PREFIX + randomLoginMemberDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_VENDOR.getMessage())))
                        .andExpect(status()
                        .isBadRequest());

        assertThat(product.getQuantity()).isEqualTo(productQuantity);
    }
}
