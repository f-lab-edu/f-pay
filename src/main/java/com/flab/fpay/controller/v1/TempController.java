package com.flab.fpay.controller.v1;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.common.response.SuccessResponse;
import com.flab.fpay.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: 2024/04/02 초기 세팅 구성 후 삭제 
@RestController
public class TempController {
    @GetMapping("/v1/test/success")
    public ResponseEntity<SuccessResponse> success() {
        return ResponseEntity.ok().body(new SuccessResponse("test"));
    }

    @GetMapping("/v1/test/fail")
    public ResponseEntity<SuccessResponse> fail() {
        throw new ApiException(ErrorCode.NOT_FOUND);
    }

    @GetMapping("/v1/test/fail/message")
    public ResponseEntity<SuccessResponse> failWithMessage() {
        throw new ApiException(ErrorCode.NOT_FOUND, "fail message");
    }

    @GetMapping("/v1/test/fail/internal")
    public ResponseEntity<SuccessResponse> internalFail() {
        throw new ApiException(ErrorCode.INTERNAL_ERROR);
    }
}
