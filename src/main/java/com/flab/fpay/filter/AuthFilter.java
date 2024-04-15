package com.flab.fpay.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.common.dto.response.FailResponse;
import com.flab.fpay.utils.auth.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.flab.fpay.domain.auth.constant.AuthConstant.*;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    public AuthFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (authorization == null || authorization.isBlank()) {
            this.createInvalidateTokenResponse(response);

            return;
        }

        String accessToken = authorization.split(BEARER_PREFIX)[1];

        try {
            Claims claims = jwtProvider.decodeToken(accessToken);

            request.setAttribute(MEMBER_ID_CLAIM_KEY, claims.get(MEMBER_ID_CLAIM_KEY));
            request.setAttribute(DEVICE_ID_CLAIM_KEY, claims.get(DEVICE_ID_CLAIM_KEY));
        } catch (ExpiredJwtException e) {
            this.createInvalidateTokenResponse(response);

            return;
        } catch (Exception e) {
            log.error("token decode exception occurred", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePath = {
                "/v1/member/sign-up",
                "/v1/member/sign-in",
                "/v1/auth/refresh",
                // TODO: 상품, 가맹점, 계좌, 결제 API 리스트 추가
        };

        String path = request.getRequestURI();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    private void createInvalidateTokenResponse(HttpServletResponse response) throws IOException {
        FailResponse failResponse = new FailResponse(ErrorCode.INVALID_TOKEN.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), failResponse);
    }
}
