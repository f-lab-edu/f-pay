package com.flab.fpay.integration.dto;

/**
 * [통합 테스트용]
 * 랜덤으로 로그인한 회원의 ID와 액세스 토큰
 */
public class RandomLoginMemberDto {
    private String accessToken;
    private long memberId;

    public RandomLoginMemberDto(String accessToken, long memberId) {
        this.accessToken = accessToken;
        this.memberId = memberId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getMemberId() {
        return memberId;
    }
}
