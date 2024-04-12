package com.flab.fpay.controller.v1.member.impl;

import com.flab.fpay.controller.v1.member.MemberController;
import com.flab.fpay.facade.member.MemberFacadeService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberControllerImpl implements MemberController {

    private final MemberFacadeService memberFacadeService;

    public MemberControllerImpl(MemberFacadeService memberFacadeService) {
        this.memberFacadeService = memberFacadeService;
    }
}
