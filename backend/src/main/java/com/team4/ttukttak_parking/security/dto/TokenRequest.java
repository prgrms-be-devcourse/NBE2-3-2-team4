package com.team4.ttukttak_parking.security.dto;

import io.swagger.v3.oas.annotations.Parameter;

public record TokenRequest (

) {
    public record Update(
        @Parameter(description = "refresh token")
        String refreshToken
    ) {

    }
}
