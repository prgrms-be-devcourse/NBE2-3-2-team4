package com.team4.ttukttak_parking.domain.member.entity.enums;

import lombok.Getter;

@Getter
public enum MemberRoles {

    ROLE_USER("general user"),
    ROLE_ADMIN("admin user");


    MemberRoles(String description){
        this.description = description;
    }

    private final String description;
}
