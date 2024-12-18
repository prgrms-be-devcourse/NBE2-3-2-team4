package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.dto.MemberDto;
import com.team4.ttukttak_parking.domain.member.dto.MemberModifyRequestDto;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
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
    public MemberDto getMemberInfo(String email ) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return MemberDto.from(member);
    }

    @Transactional
    public MemberDto modifyMember(String email, MemberModifyRequestDto memberDto) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        member.updateMember(
                memberDto.getName(),
                memberDto.getContact()
        );
        memberRepository.save(member);

        return MemberDto.from(member);

    }


}
