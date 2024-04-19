package com.flab.fpay.controller.v1.member.impl;

import com.flab.fpay.aop.LoginMemberIdCheck;
import com.flab.fpay.controller.v1.member.MemberController;
import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import com.flab.fpay.domain.member.dto.request.SignInRequest;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.dto.response.MemberDetailResponse;
import com.flab.fpay.domain.member.dto.response.SignInResponse;
import com.flab.fpay.facade.member.MemberFacadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberControllerImpl implements MemberController {
    private final MemberFacadeService memberFacadeService;

    public MemberControllerImpl(MemberFacadeService memberFacadeService) {
        this.memberFacadeService = memberFacadeService;
    }

    @Override
    @PostMapping("/v1/member/sign-up")
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        memberFacadeService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(null));
    }
    @Override
    @PostMapping("/v1/member/sign-in")
    public ResponseEntity<SuccessResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        SignInResponse signInResponse = memberFacadeService.signIn(signInRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(signInResponse));
    }

    @Override
    @GetMapping("v1/member/{memberId}")
    @LoginMemberIdCheck
    public ResponseEntity<SuccessResponse> getMemberInfo(@PathVariable long memberId) {
        MemberDetailResponse memberResponse = memberFacadeService.getMemberById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(memberResponse));
    }
}
