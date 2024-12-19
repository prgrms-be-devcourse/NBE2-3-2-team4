package com.team4.ttukttak_parking.security.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateTokenDto {

    @Parameter(description = "refresh token")
    private String refreshToken;
}
