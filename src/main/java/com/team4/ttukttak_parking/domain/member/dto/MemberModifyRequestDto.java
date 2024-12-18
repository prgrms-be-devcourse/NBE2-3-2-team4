package com.team4.ttukttak_parking.domain.member.dto;


import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberModifyRequestDto {

    private String contact;
    private String name;

}
