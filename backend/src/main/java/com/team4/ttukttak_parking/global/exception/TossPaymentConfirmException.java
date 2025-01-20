package com.team4.ttukttak_parking.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TossPaymentConfirmException extends RuntimeException {
    private final int statusCode;
    private final String msg;
}
