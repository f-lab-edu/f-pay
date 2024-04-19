package com.flab.fpay.domain.auth.dto;

import java.util.Date;

public class JwtClaimDto {
    private final Date issuedAt;
    private final Date expiredAt;
    private final long memberId;
    private final String deviceId;

    private JwtClaimDto(Date issuedAt, Date expiredAt, long memberId, String deviceId) {
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.memberId = memberId;
        this.deviceId = deviceId;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public static JwtClaimDtoBuilder builder() {
        return new JwtClaimDtoBuilder();
    }

    public static class JwtClaimDtoBuilder {
        private Date issuedAt;
        private Date expiredAt;
        private long memberId;
        private String deviceId;

        private JwtClaimDtoBuilder() {}

        public JwtClaimDtoBuilder builder() {
            return new JwtClaimDtoBuilder();
        }

        public JwtClaimDtoBuilder issuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public JwtClaimDtoBuilder expiredAt(Date expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        public JwtClaimDtoBuilder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public JwtClaimDtoBuilder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public JwtClaimDto build() {
            return new JwtClaimDto(this.issuedAt, this.expiredAt, this.memberId, this.deviceId);
        }
    }
}
