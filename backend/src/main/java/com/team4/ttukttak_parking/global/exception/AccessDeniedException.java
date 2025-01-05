package com.team4.ttukttak_parking.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AccessDeniedException extends RuntimeException {
  private final ErrorCode errorCode;
}
