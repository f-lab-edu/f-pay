package com.flab.fpay.service.member.impl;

import com.flab.fpay.domain.member.dto.MemberRegisterDto;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.repository.member.MemberJpaRepository;
import com.flab.fpay.service.member.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberServiceImpl(MemberJpaRepository repository) {
        this.memberJpaRepository = repository;
    }

    @Override
    public void registerMember(MemberRegisterDto memberRegisterDto) {
        Member member = Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(memberRegisterDto.getPassword())
                .memberType(memberRegisterDto.getMemberType())
                .balance(0)
                .isDeleted(false)
                .build();

        memberJpaRepository.save(member);
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberJpaRepository.findByEmailAndIsDeletedIsFalse(email);
    }

    @Override
    public Member getMemberById(long memberId) {
        return memberJpaRepository.findById(memberId).orElse(null);
    }
}
