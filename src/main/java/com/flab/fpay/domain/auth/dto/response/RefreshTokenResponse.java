package com.flab.fpay.domain.auth.dto.response;

public class RefreshTokenResponse {
    private final String accessToken;
    private final String refreshToken;

    private RefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static RefreshTokenResponseBuilder builder() {
        return new RefreshTokenResponseBuilder();
    }

    public static class RefreshTokenResponseBuilder {
        private String accessToken;
        private String refreshToken;

        private RefreshTokenResponseBuilder() {
        }

        public RefreshTokenResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public RefreshTokenResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenResponse build() {
            return new RefreshTokenResponse(accessToken, refreshToken);
        }
    }
}
