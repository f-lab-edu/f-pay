package com.flab.fpay.integration;

import com.flab.fpay.domain.auth.dto.JwtClaimDto;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.domain.order.entity.Order;
import com.flab.fpay.domain.order.enums.OrderStatus;
import com.flab.fpay.domain.product.entity.Product;
import com.flab.fpay.domain.product.enums.ProductCategory;
import com.flab.fpay.domain.product.enums.ProductStatus;
import com.flab.fpay.domain.vendor.entity.Vendor;
import com.flab.fpay.domain.vendor.enums.VendorStatus;
import com.flab.fpay.integration.dto.RandomLoginMemberDto;
import com.flab.fpay.repository.member.MemberJpaRepository;
import com.flab.fpay.repository.order.OrderJpaRepository;
import com.flab.fpay.repository.product.ProductJpaRepository;
import com.flab.fpay.repository.vendor.VendorJpaRepository;
import com.flab.fpay.utils.auth.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public abstract class IntegrationTest {
    @Autowired
    MemberJpaRepository memberRepository;
    @Autowired
    OrderJpaRepository orderJpaRepository;
    @Autowired
    ProductJpaRepository productJpaRepository;
    @Autowired
    VendorJpaRepository vendorJpaRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;

    protected int memberBalance = 100_000;

    protected RandomLoginMemberDto loginRandomMember() {
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        Member savedMember = this.createMember(email, password, memberBalance, MemberType.MEMBER);
        Date issuedAt = new Date();

        JwtClaimDto jwtClaimDto = JwtClaimDto.builder()
                .memberId(savedMember.getId())
                .deviceId(deviceId)
                .issuedAt(issuedAt)
                .expiredAt(getExpiredAt(issuedAt, 1))
                .build();

        return new RandomLoginMemberDto(this.createToken(jwtClaimDto), savedMember.getId());
    }

    protected Member createMember(String email, String password, int balance, MemberType memberType) {
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberType(memberType)
                .balance(balance)
                .isDeleted(false)
                .build();

        return memberRepository.saveAndFlush(member);
    }

    protected Vendor createVendor(long memberId, String bizRegNo, String name, String repNAme, String phoneNo, VendorStatus status) {
        Vendor vendor = new Vendor(memberId, bizRegNo, name, repNAme, phoneNo, status, false);

        return vendorJpaRepository.saveAndFlush(vendor);
    }

    protected Product createProduct(long vendorId,
                                    String name,
                                    String serialNo,
                                    int price,
                                    int quantity,
                                    ProductCategory category,
                                    ProductStatus status,
                                    String imgUrl) {

        Product product = new Product(vendorId, name, serialNo, quantity, price, category, status, imgUrl, false);

        return productJpaRepository.saveAndFlush(product);

    }

    protected Order createOrder(String orderUniqueId, long productId, int quantity, int amount, OrderStatus status) {
        Order order = Order.builder()
                .orderUniqueId(orderUniqueId)
                .productId(productId)
                .quantity(quantity)
                .amount(amount)
                .status(status)
                .build();

        return orderJpaRepository.saveAndFlush(order);
    }

    protected Date getExpiredAt(Date issuedAt, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issuedAt);
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }

    protected String createToken(JwtClaimDto jwtClaimDto) {
        return jwtProvider.createToken(jwtClaimDto);
    }
}
