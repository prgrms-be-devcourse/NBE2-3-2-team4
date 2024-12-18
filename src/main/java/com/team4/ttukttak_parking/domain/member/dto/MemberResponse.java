package com.team4.ttukttak_parking.domain.member.dto;

import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberResponse {


    private String contact;
    private String email;
    private LoginTypes loginType;
    private MemberRoles role;
    private String name;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public MemberResponse(String contact, String email, LoginTypes loginType, MemberRoles role, String name, LocalDateTime created_at, LocalDateTime updated_at) {
        this.contact = contact;
        this.email = email;
        this.loginType = loginType;
        this.role = role;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

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
