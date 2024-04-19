package com.flab.fpay.facade.member;

import com.flab.fpay.domain.auth.dto.AuthTokenDto;
import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.dto.MemberRegisterDto;
import com.flab.fpay.domain.member.dto.request.SignInRequest;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.dto.response.MemberDetailResponse;
import com.flab.fpay.domain.member.dto.response.SignInResponse;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.service.auth.AuthService;
import com.flab.fpay.service.member.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberFacadeService {
    private final MemberService memberService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public MemberFacadeService(MemberService memberService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void signUp(SignUpRequest request) {
        Member existingMember = memberService.getMemberByEmail(request.getEmail());

        if(existingMember != null) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        MemberRegisterDto memberRegisterDto = MemberRegisterDto
                .builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .memberType(MemberType.MEMBER)
                .build();

        memberService.registerMember(memberRegisterDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SignInResponse signIn(SignInRequest signInRequest) {
        Member member = memberService.getMemberByEmail(signInRequest.getEmail());

        if(member == null) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new ApiException(ErrorCode.MEMBER_UNAUTHORIZED);
        }

        AuthTokenDto authTokenDto =  authService.createAuthToken(member.getId(), signInRequest.getDeviceId());

        return SignInResponse.builder()
                .accessToken(authTokenDto.getAccessToken())
                .refreshToken(authTokenDto.getRefreshToken())
                .build();
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberById(long memberId) {
        Member member = memberService.getMemberById(memberId);

        if(member == null) {
            throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return MemberDetailResponse.builder()
                .email(member.getEmail())
                .memberType(member.getMemberType())
                .balance(member.getBalance())
                .build();
    }
}
