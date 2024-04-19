package com.flab.fpay.domain.auth.dto;

public class AuthTokenDto {
    private final String accessToken;
    private final String refreshToken;

    private AuthTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static AuthTokenDtoBuilder builder() {
        return new AuthTokenDtoBuilder();
    }

    public static class AuthTokenDtoBuilder {
        private String accessToken;
        private String refreshToken;

        private AuthTokenDtoBuilder() {
        }

        public AuthTokenDtoBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthTokenDtoBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthTokenDto build() {
            return new AuthTokenDto(accessToken, refreshToken);
        }
    }
}
