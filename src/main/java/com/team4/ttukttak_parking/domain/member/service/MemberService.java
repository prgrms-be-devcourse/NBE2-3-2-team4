package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.dto.MemberResponseDto;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.dto.MemberRequestDto;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberResponseDto.Join join(MemberRequestDto.Join dto) {
        // ID 유효성 체크
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

        MemberResponseDto.Join responseJoinDTO =MemberResponseDto.Join.builder()
                .contact(member.getContact())
                .email(member.getEmail())
                .name(member.getName())
                .loginType(member.getLoginType())
                .role(member.getRole())
                .build();


        return responseJoinDTO;
    }



}
