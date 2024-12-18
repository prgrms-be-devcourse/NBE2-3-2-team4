package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse.Join join(MemberRequest.Join dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateAccountException("이메일이 중복 되었습니다.");
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


}
