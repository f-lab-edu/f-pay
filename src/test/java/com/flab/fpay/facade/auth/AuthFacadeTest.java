package com.flab.fpay.facade.auth;

import com.flab.fpay.domain.auth.dto.request.RefreshTokenRequest;
import com.flab.fpay.domain.auth.entity.AuthToken;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.facade.FacadeTest;
import com.flab.fpay.repository.auth.AuthTokenJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthFacadeTest extends FacadeTest {
    @Autowired
    AuthFacadeService authFacadeService;
    @Autowired
    AuthTokenJpaRepository authTokenJpaRepository;

    @Test
    @DisplayName("토큰 재발급 성공")
    public void tokenRefresh() {
        // given
        String refreshToken = "refreshToken";
        String deviceId = "deviceId";
        long memberId = 1L;

        this.createAuthToken(memberId, refreshToken, deviceId);

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken, deviceId);

        // when
        authFacadeService.refreshToken(request);

        // then
        AuthToken result = authTokenJpaRepository.findByMemberIdAndDeviceId(memberId, deviceId);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 토큰 발급 실패")
    public void invalidRefreshToken() {
        // given
        String refreshToken = "refreshToken";
        String deviceId = "deviceId";
        long memberId = 1L;

        this.createAuthToken(memberId, refreshToken, deviceId);

        RefreshTokenRequest request = new RefreshTokenRequest("invalid refresh token", deviceId);

        // when & then
        assertThatThrownBy(() -> authFacadeService.refreshToken(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.INVALID_TOKEN.getMessage());
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
