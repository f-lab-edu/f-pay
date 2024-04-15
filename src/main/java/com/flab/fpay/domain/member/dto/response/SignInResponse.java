package com.flab.fpay.domain.member.dto.response;

public class SignInResponse {
    private final String accessToken;
    private final String refreshToken;

    private SignInResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static SignInResponseBuilder builder() {
        return new SignInResponseBuilder();
    }

    public static class SignInResponseBuilder {
        private String accessToken;
        private String refreshToken;

        private SignInResponseBuilder() {
        }

        public SignInResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public SignInResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public SignInResponse build() {
            return new SignInResponse(accessToken, refreshToken);
        }
    }
}
