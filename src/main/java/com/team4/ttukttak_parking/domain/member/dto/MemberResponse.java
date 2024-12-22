package com.team4.ttukttak_parking.domain.member.dto;

import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import java.time.LocalDateTime;

public record MemberResponse() {

    public record Join(
        String name,
        String email,
        String contact,
        LoginTypes loginType,
        MemberRoles role

    ) {

        public static Join from(Member member) {
            return new Join(member.getName(), member.getEmail(), member.getContact(),
                member.getLoginType(), member.getRole());
        }
    }

    public record GetMember(
        String name,
        String email,
        String contact,
        LoginTypes loginType,
        MemberRoles role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

        public static GetMember from(Member member) {
            return new GetMember(
                member.getName(), member.getEmail(), member.getContact(), member.getLoginType(),
                member.getRole(), member.getCreatedAt(), member.getUpdatedAt());
        }
    }



}
