package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 일반 user 회원가입
    @Transactional
    public MemberResponse.Join join(MemberRequest.Join dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST);
        }

        return MemberResponse.Join.from(
            memberRepository.save(Member.to(dto, passwordEncoder, MemberRoles.ROLE_USER)));
    }

    // Admin 회원가입
    @Transactional
    public MemberResponse.Join joinAdmin(MemberRequest.Join dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST);
        }

        return MemberResponse.Join.from(
            memberRepository.save(Member.to(dto, passwordEncoder, MemberRoles.ROLE_ADMIN)));
    }

    @Transactional(readOnly = true)
    public MemberResponse.Read getMemberInfo(String email) {
        return MemberResponse.Read.from(
            memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public Void modifyInfo(MemberRequest.Modify dto, String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        member.updateMember(dto.contact(), dto.name());

        return null;
    }


}
