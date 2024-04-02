package com.flab.fpay.facade.member;

import com.flab.fpay.service.member.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberFacadeService {

    private final MemberService memberService;

    public MemberFacadeService(MemberService memberService) {
        this.memberService = memberService;
    }
}
