package com.flab.fpay.domain.member.dto.response;

import com.flab.fpay.domain.member.enums.MemberType;

public class MemberDetailResponse {
    private final String email;
    private final MemberType memberType;
    private final int balance;

    private MemberDetailResponse(String email, MemberType memberType, int balance) {
        this.email = email;
        this.memberType = memberType;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public int getBalance() {
        return balance;
    }

    public static MemberDetailResponseBuilder builder() {
        return new MemberDetailResponseBuilder();
    }

    public static class MemberDetailResponseBuilder {
        private String email;
        private MemberType memberType;
        private int balance;
        private MemberDetailResponseBuilder() {
        }

        public MemberDetailResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberDetailResponseBuilder memberType(MemberType memberType) {
            this.memberType = memberType;
            return this;
        }

        public MemberDetailResponseBuilder balance(int balance) {
            this.balance = balance;
            return this;
        }

        public MemberDetailResponse build() {
            return new MemberDetailResponse(this.email, this.memberType, this.balance);
        }
    }

}
