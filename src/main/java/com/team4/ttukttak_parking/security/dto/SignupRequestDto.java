package com.team4.ttukttak_parking.security.dto;

import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignupRequestDto {
    private String contact;
    private String email;
    private String name;
    private String password;

}
