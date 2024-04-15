package com.flab.fpay.integration.member.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.fpay.domain.auth.entity.AuthToken;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.dto.request.SignInRequest;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.integration.IntegrationTest;
import com.flab.fpay.repository.auth.AuthTokenJpaRepository;
import com.flab.fpay.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberIntegrationV1Test extends IntegrationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberJpaRepository memberRepository;

    @Autowired
    AuthTokenJpaRepository authTokenJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccess() throws Exception {
        // given
        String email = "test@email";
        String password = "password";

        SignUpRequest request = new SignUpRequest(email, password);

        // when & then
        mockMvc.perform(post("/v1/member/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Member result = memberRepository.findAll().getFirst();

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getBalance()).isZero();
        assertThat(result.getDeleted()).isFalse();
    }

    @Test
    @DisplayName("회원가입 이메일 검증 실패")
    public void signUpEmailValidationFail() throws Exception {
        // given
        String email = "test";
        String password = "password";

        SignUpRequest request = new SignUpRequest(email, password);

        // when & then
        mockMvc.perform(post("/v1/member/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 비밀번호 검증 실패")
    public void signUpPasswordValidationFail() throws Exception {
        // given
        String email = "test@gmail.com";
        String password = "";

        SignUpRequest request = new SignUpRequest(email, password);

        // when & then
        mockMvc.perform(post("/v1/member/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(status()
                .isBadRequest());
    }

    @Test
    @DisplayName("회원가입 이메일 중복")
    public void signUpDuplicatedEmail() throws Exception {
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
        mockMvc.perform(post("/v1/member/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("duplicated email address")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공")
    void memberSignInSuccess() throws Exception {
        // given
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        Member savedMember = this.createMember(email, password);

        SignInRequest request = new SignInRequest(email, password, deviceId);

        // when & then
        mockMvc.perform(post("/v1/member/sign-in")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.payload.accessToken", notNullValue()))
                        .andExpect(jsonPath("$.payload.refreshToken", notNullValue()))
                        .andExpect(status()
                        .isOk());

        AuthToken result = authTokenJpaRepository.findByMemberIdAndDeviceId(savedMember.getId(), deviceId);

        assertThat(result.getMemberId()).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("로그인 이메일 검증 실패")
    public void signInEmailValidationFail() throws Exception {
        // given
        String email = "test";
        String password = "password";
        String deviceId = "deviceId";

        SignInRequest request = new SignInRequest(email, password, deviceId);

        // when & then
        mockMvc.perform(post("/v1/member/sign-in")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 비밀번호 불일치")
    void memberSignInInvalidPassword() throws Exception {
        // given
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        this.createMember(email, password);

        SignInRequest request = new SignInRequest(email, "invalid password", deviceId);

        // when & then
        mockMvc.perform(post("/v1/member/sign-in")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    @DisplayName("로그인 이메일 불일치")
    void memberSignInInvalidEmail() throws Exception {
        // given
        String email = "test@email";
        String password = "password";
        String deviceId = "deviceId";

        this.createMember(email, password);

        SignInRequest request = new SignInRequest("invalid@email", password, deviceId);

        // when & then
        mockMvc.perform(post("/v1/member/sign-in")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$.message", is(ErrorCode.MEMBER_NOT_FOUND.getMessage())))
                        .andExpect(status()
                        .isNotFound());
    }

    private Member createMember(String email, String password) {
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberType(MemberType.MEMBER)
                .balance(0)
                .isDeleted(false)
                .build();

        return memberRepository.save(member);
    }
}
