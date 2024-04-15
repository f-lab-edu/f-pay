package com.flab.fpay.domain.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // common
    NOT_FOUND(HttpStatus.NOT_FOUND, "resource not found"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal error"),

    // member
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "duplicated email address"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "member not found"),
    MEMBER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "member unauthorized"),

    // auth
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "invalid token");

    // member
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "duplicated email address"),

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
