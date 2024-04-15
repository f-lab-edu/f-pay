package com.flab.fpay.controller.v1.member.impl;

import com.flab.fpay.controller.v1.member.MemberController;
import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.facade.member.MemberFacadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberControllerImpl implements MemberController {

    private final MemberFacadeService memberFacadeService;

    public MemberControllerImpl(MemberFacadeService memberFacadeService) {
        this.memberFacadeService = memberFacadeService;
    }

    @PostMapping("/v1/member/sign-up")
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        memberFacadeService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(null));
    }
}
