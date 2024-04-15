package com.flab.fpay.controller.v1.auth.impl;

import com.flab.fpay.controller.v1.auth.AuthController;
import com.flab.fpay.domain.auth.dto.request.RefreshTokenRequest;
import com.flab.fpay.domain.auth.dto.response.RefreshTokenResponse;
import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import com.flab.fpay.facade.auth.AuthFacadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthFacadeService authFacadeService;

    public AuthControllerImpl(AuthFacadeService authFacadeService) {
        this.authFacadeService = authFacadeService;
    }

    @Override
    @PostMapping("/v1/auth/refresh")
    public ResponseEntity<SuccessResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse refreshTokenResponse = authFacadeService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(refreshTokenResponse));
    }
}
