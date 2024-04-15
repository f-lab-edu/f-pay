package com.flab.fpay.repository.auth;

import com.flab.fpay.domain.auth.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenJpaRepository extends JpaRepository<AuthToken, Long> {
    AuthToken findByMemberIdAndDeviceId(Long memberId, String deviceId);

    AuthToken findByRefreshTokenAndDeviceId(String refreshToken, String deviceId);
}
