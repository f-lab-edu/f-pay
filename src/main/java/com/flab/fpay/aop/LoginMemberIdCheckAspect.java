package com.flab.fpay.aop;

import com.flab.fpay.domain.auth.constant.AuthConstant;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoginMemberIdCheckAspect {

    /**
     * 엑세스 토큰에서 추출한 memberId와 실제 요청의 memberId가 일치하는지 검사
     */
    @Before("@annotation(com.flab.fpay.aop.LoginMemberIdCheck) && args(memberId)")
    public void checkLoginMemberId(final long memberId) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        long authMemberId = Long.parseLong(req.getAttribute(AuthConstant.MEMBER_ID_CLAIM_KEY).toString());

        if (authMemberId != memberId) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "invalid member request");
        }
    }
}
