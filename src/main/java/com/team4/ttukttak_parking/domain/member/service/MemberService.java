package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse.Join join(MemberRequest.Join dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST);
        }

        return MemberResponse.Join.from(memberRepository.save(Member.to(dto)));
    }

    @Transactional(readOnly = true)
    public MemberResponse.Read getMemberInfo(String email ) {
        return MemberResponse.Read.from(
            memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public Void modifyMember(MemberRequest.Modify modifyInfo) {
        Member member = memberRepository.findByEmail(modifyInfo.getEmail())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        member.updateMember(
                modifyInfo.getContact(),
                modifyInfo.getEmail(),
                modifyInfo.getName(),
                modifyInfo.getPassword()
        );

        return null;
    }




}
