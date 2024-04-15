package com.flab.fpay.domain.member.dto;

import com.flab.fpay.domain.member.enums.MemberType;

public class MemberRegisterDto {
    private final String email;
    private final String password;
    private final MemberType memberType;

    private MemberRegisterDto(String email, String password, MemberType memberType) {
        this.email = email;
        this.password = password;
        this.memberType = memberType;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public static MemberRegisterDtoBuilder builder() {
        return new MemberRegisterDtoBuilder();
    }

    public static class MemberRegisterDtoBuilder {
        private String email;
        private String password;
        private MemberType memberType;

        private MemberRegisterDtoBuilder() {
        }

        public MemberRegisterDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberRegisterDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberRegisterDtoBuilder memberType(MemberType memberType) {
            this.memberType = memberType;
            return this;
        }

        public MemberRegisterDto build() {
            return new MemberRegisterDto(this.email, this.password, this.memberType);
        }
    }
}
