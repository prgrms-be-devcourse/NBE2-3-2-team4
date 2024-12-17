package com.team4.ttukttak_parking.domain.member.dto;


import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberRequestDto {

    @Data
    public static class Join {
        private String contact;
        private String email;
        private LoginTypes loginType;
        private MemberRoles role;
        private String name;
        private String password;
    }

}
