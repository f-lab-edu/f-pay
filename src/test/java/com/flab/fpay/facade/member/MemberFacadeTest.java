package com.flab.fpay.facade.member;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.dto.request.SignInRequest;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.dto.response.SignInResponse;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.facade.FacadeTest;
import com.flab.fpay.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberFacadeTest extends FacadeTest {
    @Autowired
    MemberFacadeService memberFacadeService;
    @Autowired
    MemberJpaRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccess() {
        // given
        String email = "test@email";
        String password = "password";

        SignUpRequest request = new SignUpRequest(email, password);

        // when
        memberFacadeService.signUp(request);

        // then
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

    @Test
    @DisplayName("로그인 성공")
    public void signInSuccess() {
        // given
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberType(MemberType.MEMBER)
                .balance(0)
                .isDeleted(false)
                .build();

        memberRepository.save(member);

        // when
        SignInRequest request = new SignInRequest(email, password, deviceId);
        SignInResponse result = memberFacadeService.signIn(request);

        // then
        assertThat(result.getAccessToken()).isNotNull();
        assertThat(result.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("로그인 비밀번호 불일치")
    public void memberSignInInvalidPassword() {
        // given
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberType(MemberType.MEMBER)
                .balance(0)
                .isDeleted(false)
                .build();

        memberRepository.save(member);

        SignInRequest request = new SignInRequest(email, "invalid password", deviceId);

        // when & then
        assertThatThrownBy(() -> memberFacadeService.signIn(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.MEMBER_UNAUTHORIZED.getMessage());
    }

    @Test
    @DisplayName("로그인 이메일 불일치")
    public void memberSignInInvalidEmail() {
        // given
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberType(MemberType.MEMBER)
                .balance(0)
                .isDeleted(false)
                .build();

        memberRepository.save(member);

        SignInRequest request = new SignInRequest("invalid@email", password, deviceId);

        // when & then
        assertThatThrownBy(() -> memberFacadeService.signIn(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }
}
