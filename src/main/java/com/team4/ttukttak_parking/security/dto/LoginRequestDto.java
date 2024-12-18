package com.team4.ttukttak_parking.security.dto;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

@RequiredArgsConstructor
@Getter
public class LoginRequestDto {
    @Parameter(description = "email")
    String email;
    @Parameter(description = "비밀번호")
    String password;
}
