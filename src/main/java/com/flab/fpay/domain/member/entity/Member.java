package com.flab.fpay.domain.member.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
import com.flab.fpay.domain.member.enums.MemberType;
import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "member_type")
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Column(name = "balance")
    private int balance;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    protected Member() {
    }

    private Member(String email, String password, MemberType memberType, int balance, Boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.memberType = memberType;
        this.balance = balance;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public static class MemberBuilder {
        private String email;
        private String password;
        private MemberType memberType;
        private int balance;
        private boolean isDeleted;

        private MemberBuilder() {
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder memberType(MemberType memberType) {
            this.memberType = memberType;
            return this;
        }

        public MemberBuilder balance(int balance) {
            this.balance = balance;
            return this;
        }

        public MemberBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Member build() {
            return new Member(this.email, this.password, this.memberType, this.balance, this.isDeleted);
        }
    }
}
