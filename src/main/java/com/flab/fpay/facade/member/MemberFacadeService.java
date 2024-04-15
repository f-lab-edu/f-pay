package com.flab.fpay.facade.member;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.member.dto.MemberRegisterDto;
import com.flab.fpay.domain.member.dto.request.SignUpRequest;
import com.flab.fpay.domain.member.entity.Member;
import com.flab.fpay.domain.member.enums.MemberType;
import com.flab.fpay.exception.ApiException;
import com.flab.fpay.service.member.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberFacadeService {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public MemberFacadeService(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
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
}
