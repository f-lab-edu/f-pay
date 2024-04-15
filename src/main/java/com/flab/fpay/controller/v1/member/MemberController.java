package com.flab.fpay.controller.v1.member;

import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import com.flab.fpay.domain.member.dto.request.SignInRequest;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    ResponseEntity<SuccessResponse> signUp(SignUpRequest request);
    ResponseEntity<SuccessResponse> signIn(SignInRequest request);
}
