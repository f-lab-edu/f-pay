package com.flab.fpay.service.auth;

import com.flab.fpay.domain.auth.dto.AuthTokenDto;
import com.flab.fpay.domain.auth.entity.AuthToken;

public interface AuthService {
    AuthTokenDto createAuthToken(long memberId, String deviceId);
    AuthToken getTokenByRefreshToken(String refreshToken, String deviceId);
    void deleteExistingToken(long authTokenId);
}
