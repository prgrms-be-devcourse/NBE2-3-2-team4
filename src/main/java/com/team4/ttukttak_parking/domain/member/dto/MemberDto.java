package com.team4.ttukttak_parking.domain.member.dto;

import com.team4.ttukttak_parking.domain.member.entity.Member;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class MemberDto {

    private String contact;
    private String email;
    private String name;

    public static MemberDto from(Member member){
        return new MemberDto(member.getContact(),member.getEmail(),member.getName());
    }


}
