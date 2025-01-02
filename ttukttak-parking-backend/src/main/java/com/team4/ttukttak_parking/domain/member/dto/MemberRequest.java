package com.team4.ttukttak_parking.domain.member.dto;


import io.swagger.v3.oas.annotations.Parameter;

public record MemberRequest() {


    public record Join(
        String contact,
        String email,
        String name,
        String password
    ) {

    }

    public record Login(
        @Parameter(description = "email")
        String email,
        @Parameter(description = "비밀번호")
        String password
    ) {

    }


    public record Modify(
        String contact,
        String name) {

    }


}
