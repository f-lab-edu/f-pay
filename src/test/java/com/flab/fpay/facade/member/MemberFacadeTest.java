package com.flab.fpay.facade.member;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.facade.FacadeTest;
import com.flab.fpay.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberFacadeTest extends FacadeTest {
    @Autowired
    MemberFacadeService memberFacadeService;
    @Autowired
    MemberJpaRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccessTest() {
        // given
        String email = "test@email";
        String password = "password";

        SignUpRequest request = new SignUpRequest(email, password);

        // when
        memberFacadeService.signUp(request);

        Member result = memberRepository.findAll().getFirst();

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getBalance()).isZero();
        assertThat(result.getDeleted()).isFalse();
    }

    @Test
    @DisplayName("회원가입 이메일 중복 예외")
    public void signUpDuplicatedEmail() {
        // given
        String email = "test@gmail";
        String password = "password";

        SignUpRequest request = new SignUpRequest(email, password);

        Member member = Member.builder()
                .email(email)
                .password(password)
                .memberType(MemberType.MEMBER)
                .balance(0)
                .isDeleted(false)
                .build();

        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberFacadeService.signUp(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.DUPLICATED_EMAIL.getMessage());
    }
}
