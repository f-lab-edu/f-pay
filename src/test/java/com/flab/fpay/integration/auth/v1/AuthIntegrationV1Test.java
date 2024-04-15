package com.flab.fpay.integration.auth.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.fpay.domain.auth.dto.request.RefreshTokenRequest;
import com.flab.fpay.domain.auth.entity.AuthToken;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.integration.IntegrationTest;
import com.flab.fpay.repository.auth.AuthTokenJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationV1Test extends IntegrationTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AuthTokenJpaRepository authTokenJpaRepository;

    @Test
    @DisplayName("토큰 재발급 성공")
    public void tokenRefresh() throws Exception {
        // given
        String refreshToken = "refreshToken";
        String deviceId = "deviceId";
        long memberId = 1L;

        this.createAuthToken(memberId, refreshToken, deviceId);

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken, deviceId);

        // when & then
        mockMvc.perform(post("/v1/auth/refresh")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.payload.accessToken", notNullValue()))
                .andExpect(jsonPath("$.payload.refreshToken", notNullValue()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 토큰 발급 실패")
    public void invalidRefreshToken() throws Exception {
        // given
        String refreshToken = "refreshToken";
        String deviceId = "deviceId";
        long memberId = 1L;

        this.createAuthToken(memberId, refreshToken, deviceId);

        RefreshTokenRequest request = new RefreshTokenRequest("invalid refresh token", deviceId);

        // when & then
        mockMvc.perform(post("/v1/auth/refresh")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_TOKEN.getMessage())))
                .andExpect(status().isBadRequest());
    }

    private AuthToken createAuthToken(long memberId, String refreshToken, String deviceId) {
        AuthToken authToken = AuthToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .issuedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMonths(1))
                .build();

        return authTokenJpaRepository.save(authToken);
    }
}
