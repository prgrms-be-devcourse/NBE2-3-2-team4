package com.team4.ttukttak_parking.domain.member.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private long member_id;
    private String contact;
    private String created_at;
    private String email;
    private String login_type;
    private String role;
    private String name;
    private String password;
    private String update_at;


    @Data
    public static class ResponseJoinDTO{
        private String name;
        private String email;
    }

}
