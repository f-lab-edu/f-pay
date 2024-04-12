package com.flab.fpay.domain.member.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
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
    private String memberType;

    @Column(name = "balance")
    private int balance;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
