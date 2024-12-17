package com.team4.ttukttak_parking.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DuplicateAccountException extends RuntimeException{
    private final String message;
}
