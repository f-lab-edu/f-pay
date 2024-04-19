package com.flab.fpay.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignInRequest {
    @Email
    private final String email;
    @NotBlank
    private final String password;
    @NotNull
    private final String deviceId;

    public SignInRequest(String email, String password, String deviceId) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDeviceId() {
        return deviceId;
    }
}