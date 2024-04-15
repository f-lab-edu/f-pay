package com.flab.fpay.service.member;

import com.flab.fpay.domain.member.dto.MemberRegisterDto;
import com.flab.fpay.domain.member.entity.Member;

public interface MemberService {
    void registerMember(MemberRegisterDto memberRegisterDto);

    Member getMemberByEmail(String email);
}
