package com.flab.fpay.service.auth.impl;

import com.flab.fpay.domain.auth.dto.AuthTokenDto;
import com.flab.fpay.domain.auth.dto.JwtClaimDto;
import com.flab.fpay.domain.auth.entity.AuthToken;
import com.flab.fpay.repository.auth.AuthTokenJpaRepository;
import com.flab.fpay.service.auth.AuthService;
import com.flab.fpay.utils.auth.JwtProvider;
import com.flab.fpay.utils.common.DateTimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final AuthTokenJpaRepository authTokenJpaRepository;
    @Value("${auth.access-token-expired-hours}")
    private int accessTokenExpiredHours;
    @Value("${auth.refresh-token-expired-hours}")
    private int refreshTokenExpiredHours;

    public AuthServiceImpl(JwtProvider jwtProvider, AuthTokenJpaRepository authTokenJpaRepository) {
        this.jwtProvider = jwtProvider;
        this.authTokenJpaRepository = authTokenJpaRepository;
    }

    @Override
    public AuthTokenDto createAuthToken(long memberId, String deviceId) {
        Date issuedAt = new Date();

        JwtClaimDto accessTokenClaim = JwtClaimDto.builder()
                .issuedAt(issuedAt)
                .expiredAt(getExpiredAt(issuedAt, accessTokenExpiredHours))
                .memberId(memberId)
                .deviceId(deviceId)
                .build();

        JwtClaimDto refreshTokenClaim = JwtClaimDto.builder()
                .issuedAt(issuedAt)
                .expiredAt(getExpiredAt(issuedAt, refreshTokenExpiredHours))
                .memberId(memberId)
                .deviceId(deviceId)
                .build();

        String accessToken = jwtProvider.createToken(accessTokenClaim);
        String refreshToken = jwtProvider.createToken(refreshTokenClaim);

        AuthToken authToken = AuthToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .issuedAt(DateTimeUtils.dateToLocalDateTime(issuedAt))
                .expiredAt(DateTimeUtils.dateToLocalDateTime(getExpiredAt(issuedAt, refreshTokenExpiredHours)))
                .build();

        authTokenJpaRepository.save(authToken);

        return AuthTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthToken getTokenByRefreshToken(String refreshToken, String deviceId) {
        return authTokenJpaRepository.findByRefreshTokenAndDeviceId(refreshToken, deviceId);
    }

    @Override
    public void deleteExistingToken(long authTokenId) {
        authTokenJpaRepository.deleteById(authTokenId);
    }

    private Date getExpiredAt(Date issuedAt, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issuedAt);
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }
}
