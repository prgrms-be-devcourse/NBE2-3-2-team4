package com.team4.ttukttak_parking.domain.member.dto;

import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import lombok.Builder;
import lombok.Data;

public class MemberResponseDto {



    @Data
    @Builder

    public static class Join{
        private String contact;
        private String email;
        private LoginTypes loginType;
        private MemberRoles role;
        private String name;

        public Join(String contact, String email, LoginTypes loginType, MemberRoles role, String name) {
            this.contact = contact;
            this.email = email;
            this.loginType = loginType;
            this.role = role;
            this.name = name;
        }
    }


}
