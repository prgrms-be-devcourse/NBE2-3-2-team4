package com.team4.ttukttak_parking.domain.member.dto;


import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import lombok.Data;

public record MemberRequest (){


    public record Join(
        String contact,
        String email,
        LoginTypes loginType,
        MemberRoles role,
        String name,
        String password
    ) {

    }


    @Data
    public static class Modify {

        private String contact;
        private String email;
        private LoginTypes loginType;
        private MemberRoles role;
        private String name;
        private String password;
    }


}
