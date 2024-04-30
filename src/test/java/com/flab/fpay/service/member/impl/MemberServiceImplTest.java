package com.flab.fpay.service.member.impl;

import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceImplTest {
    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    long memberId;

    @BeforeEach
    public void before() {
        int balance = 100_000;

        Member member = Member.builder()
                .email("test@gmail.com")
                .password("password")
                .memberType(MemberType.MEMBER)
                .balance(balance)
                .isDeleted(false)
                .build();

        this.memberId = memberJpaRepository.save(member).getId();
    }

    @AfterEach
    public void after() {
        memberJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 페이머니 감소")
    void decreasePaymoney() {
        // given & when
        memberService.decreasePaymoney(memberId, 1_000);

        // then
        Member member = memberJpaRepository.findById(memberId).get();

        assertThat(member.getBalance()).isEqualTo(99_000);
    }

    @Test
    @DisplayName("동시에 100개의 요청")
    void decreasePaymoneyMultiThread() throws InterruptedException {
        // given & when
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    memberService.decreasePaymoney(memberId, 1_000);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Member member = memberJpaRepository.findById(memberId).get();

        assertThat(member.getBalance()).isZero();
    }
}