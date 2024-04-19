package com.flab.fpay.controller.v1.auth;

import com.flab.fpay.domain.auth.dto.request.RefreshTokenRequest;
import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    ResponseEntity<SuccessResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
