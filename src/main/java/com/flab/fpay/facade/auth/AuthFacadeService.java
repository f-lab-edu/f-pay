package com.flab.fpay.facade.auth;

import com.flab.fpay.domain.auth.dto.AuthTokenDto;
import com.flab.fpay.domain.auth.dto.request.RefreshTokenRequest;
import com.flab.fpay.domain.auth.dto.response.RefreshTokenResponse;
import com.flab.fpay.domain.auth.entity.AuthToken;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.service.auth.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthFacadeService {
    private final AuthService authService;

    public AuthFacadeService(AuthService authService) {
        this.authService = authService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        AuthToken existingAuthToken =
                authService.getTokenByRefreshToken(refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getDeviceId());

        if (existingAuthToken == null) {
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }

        AuthTokenDto newAuthToken =
                authService.createAuthToken(existingAuthToken.getMemberId(), refreshTokenRequest.getDeviceId());

        authService.deleteExistingToken(existingAuthToken.getId());

        return RefreshTokenResponse.builder()
                .accessToken(newAuthToken.getAccessToken())
                .refreshToken(newAuthToken.getRefreshToken())
                .build();
    }
}
