package com.flab.fpay.integration.member.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.integration.IntegrationTest;
import com.flab.fpay.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

        Member member = new Member(email, password, MemberType.MEMBER, 0, false);
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
}
