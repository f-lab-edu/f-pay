package com.flab.fpay.controller.v1.payment.impl;

import com.flab.fpay.domain.common.dto.response.SuccessResponse;
import com.flab.fpay.domain.payment.dto.request.PaymentRequest;
import com.flab.fpay.facade.payment.PaymentFacadeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.flab.fpay.domain.auth.constant.AuthConstant.MEMBER_ID_CLAIM_KEY;

@RestController
public class PaymentControllerImpl {

    private final PaymentFacadeService paymentFacadeService;

    public PaymentControllerImpl(PaymentFacadeService paymentFacadeService) {
        this.paymentFacadeService = paymentFacadeService;
    }

    @PostMapping("/v1/payments/pay")
    public ResponseEntity<SuccessResponse> requestPayment(@RequestBody @Valid PaymentRequest paymentRequest,
                                                          HttpServletRequest request) {
        long memberId = Long.parseLong(request.getAttribute(MEMBER_ID_CLAIM_KEY).toString());
        paymentFacadeService.requestPayment(paymentRequest, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(null));
    }
}
