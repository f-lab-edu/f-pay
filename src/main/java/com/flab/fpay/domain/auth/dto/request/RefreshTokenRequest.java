package com.flab.fpay.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public class RefreshTokenRequest {
    @NotNull
    private String refreshToken;
    @NotNull
    private String deviceId;

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public RefreshTokenRequest(String refreshToken, String deviceId) {
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
    }
}
