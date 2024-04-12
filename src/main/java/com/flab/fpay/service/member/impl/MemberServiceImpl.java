package com.flab.fpay.service.member.impl;

import com.flab.fpay.repository.member.MemberJpaRepository;
import com.flab.fpay.service.member.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberServiceImpl(MemberJpaRepository repository) {
        this.memberJpaRepository = repository;
    }
}
