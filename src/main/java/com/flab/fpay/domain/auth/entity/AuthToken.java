package com.flab.fpay.domain.auth.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "auth_token",
        indexes = {
                @Index(name = "uidx_at_refreshtoken", columnList = "refresh_token", unique = true)
        }
)
public class AuthToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "member_id")
    private long memberId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    protected AuthToken() {
    }

    private AuthToken(long memberId, String refreshToken, String deviceId, LocalDateTime issuedAt, LocalDateTime expiredAt) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public static AuthTokenBuilder builder() {
        return new AuthTokenBuilder();
    }

    public static class AuthTokenBuilder {
        private long memberId;
        private String refreshToken;
        private String deviceId;
        private LocalDateTime issuedAt;
        private LocalDateTime expiredAt;
        private AuthTokenBuilder() {}

        public AuthTokenBuilder builder() {
            return new AuthTokenBuilder();
        }
        public AuthTokenBuilder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public AuthTokenBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthTokenBuilder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public AuthTokenBuilder issuedAt(LocalDateTime issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public AuthTokenBuilder expiredAt(LocalDateTime expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        public AuthToken build() {
            return new AuthToken(this.memberId, this.refreshToken, this.deviceId, this.issuedAt, this.expiredAt);
        }
    }
}