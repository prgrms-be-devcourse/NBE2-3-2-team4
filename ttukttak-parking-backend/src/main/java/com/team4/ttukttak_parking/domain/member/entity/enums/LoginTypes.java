package com.team4.ttukttak_parking.domain.member.entity.enums;

import lombok.Getter;

@Getter
public enum LoginTypes {


    KAKAO("kakao login"),
    BASIC("email login");

    LoginTypes(String description){
        this.description = description;
    }
    private final String description;



}
