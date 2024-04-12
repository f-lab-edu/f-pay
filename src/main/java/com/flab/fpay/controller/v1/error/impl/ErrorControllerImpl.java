package com.flab.fpay.controller.v1.error.impl;

import com.flab.fpay.domain.common.dto.response.FailResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ErrorControllerImpl extends AbstractErrorController {
    private final ErrorAttributeOptions errorAttributeOptions;

    public ErrorControllerImpl(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributeOptions = ErrorAttributeOptions.of();
    }

    @GetMapping("/error")
    public ResponseEntity<FailResponse> handleError(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, errorAttributeOptions);

        FailResponse failResponse = new FailResponse(errorAttributes.get("error").toString());
        HttpStatus status = getStatus(request);

        return ResponseEntity.status(status).body(failResponse);
    }
}
