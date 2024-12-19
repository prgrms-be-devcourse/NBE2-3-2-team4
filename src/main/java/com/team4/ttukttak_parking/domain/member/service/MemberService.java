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
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST);
        }

        Member member = Member.builder()
            .email(dto.getEmail())
            .password(dto.getPassword())
            .name(dto.getName())
            .contact(dto.getContact())
            .role(dto.getRole())
            .loginType(dto.getLoginType())
            .build();

        memberRepository.save(member);

        return MemberResponse.Join.builder()
            .contact(member.getContact())
            .email(member.getEmail())
            .name(member.getName())
            .loginType(member.getLoginType())
            .role(member.getRole())
            .build();
    }

    @Transactional
    public MemberResponse getMemberInfo(String email ) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return MemberResponse.builder()
                .contact(member.getContact())
                .email(member.getEmail())
                .loginType(member.getLoginType())
                .role(member.getRole())
                .name(member.getName())
                .created_at(member.getCreatedAt())
                .updated_at(member.getUpdatedAt())
                .build();
    }

    @Transactional
    public MemberResponse modifyMember(MemberRequest.Modify modifyInfo) {

        Member member = memberRepository.findByEmail(modifyInfo.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // Entity 내부 선언한 메소드를통해 업데이트 할수 있도록 수정
        member.updateMember(
                modifyInfo.getContact(),
                modifyInfo.getEmail(),
                modifyInfo.getName(),
                modifyInfo.getPassword()
        );

        return MemberResponse.builder()
                .contact(member.getContact())
                .email(member.getEmail())
                .loginType(member.getLoginType())
                .role(member.getRole())
                .name(member.getName())
                .created_at(member.getCreatedAt())
                .updated_at(member.getUpdatedAt())
                .build();

    }




}
