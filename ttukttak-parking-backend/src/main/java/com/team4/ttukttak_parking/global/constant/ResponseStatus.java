package com.team4.ttukttak_parking.global.constant;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS("성공"),
    ERROR("오류");

    private final String msg;

    ResponseStatus(String msg) {
        this.msg= msg;
    }
}
