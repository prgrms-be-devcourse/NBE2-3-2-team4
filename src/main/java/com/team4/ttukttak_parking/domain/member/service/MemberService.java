package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.entity.MemberDTO;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.team4.ttukttak_parking.global.response.ApiResponse.createErrorWithMsg;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberDTO.ResponseJoinDTO join(Member member) {
        // ID 유효성 체크
        if (!emailDupicationCheck(member.getEmail())) {
            throw new DuplicateAccountException("이메일이 중복 되었습니다.");
        }

        Member newMember = memberRepository.save(member);
        MemberDTO.ResponseJoinDTO responseJoinDTO = new MemberDTO.ResponseJoinDTO();
        responseJoinDTO.setName(newMember.getName());
        responseJoinDTO.setEmail(newMember.getEmail());

        return responseJoinDTO;
    }

    public boolean emailDupicationCheck(String email) {
        long count = memberRepository.countByEmail(email);
        if (count > 0) {
            return false ;
        }else{
            return true;
        }

    }

}