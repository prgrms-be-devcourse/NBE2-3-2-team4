package com.team4.ttukttak_parking.security.controller;


import com.team4.ttukttak_parking.global.constant.ResponseStatus;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import com.team4.ttukttak_parking.security.dto.LoginRequestDto;
import com.team4.ttukttak_parking.security.dto.SignupRequestDto;
import com.team4.ttukttak_parking.security.dto.TokenDto;
import com.team4.ttukttak_parking.security.dto.UpdateTokenDto;
import com.team4.ttukttak_parking.security.service.CustomUserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "😎 User-Auth", description = "사용자 인증 인가 관련 API - 별도의 인증 없이 접근 가능")
public class AuthController {

    private final CustomUserDetailService userDetailService;


    @Operation(summary = "회원가입 API", description = "회원 가입을 진행합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> joinMember(@RequestBody SignupRequestDto dto) {

        userDetailService.join(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .status(ResponseStatus.SUCCESS.getMsg())
                        .message("member created")
                        .data(null).build());
    }

    @Operation(summary = "token update api", description = "refresh token을 통해 at & rt 모두 update")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<TokenDto>> updateTokens(@RequestBody UpdateTokenDto updateTokenDto){
        TokenDto tokenDto =  userDetailService.updateToken(updateTokenDto.getRefreshToken());
        return ResponseEntity.ok().body(ApiResponse.createSuccess(tokenDto));
    }

    @Operation(summary = "login api", description = "회원 로그인 진행 & jwt token 발급")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody LoginRequestDto loginRequestDto){
        TokenDto tokenDto = userDetailService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        return ResponseEntity.ok().body(ApiResponse.createSuccess(tokenDto));
    }

}
