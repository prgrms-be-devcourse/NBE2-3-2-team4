package com.team4.ttukttak_parking.controller;


import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.member.service.MemberService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import com.team4.ttukttak_parking.security.dto.TokenRequest;
import com.team4.ttukttak_parking.security.dto.TokenResponse;
import com.team4.ttukttak_parking.security.service.CustomUserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "😎 Auth", description = "사용자 인증 인가 관련 API - 별도의 인증 없이 접근 가능")
public class AuthController {

    private final CustomUserDetailService userDetailService;
    private final MemberService memberService;

    @Operation(summary = "회원가입 API", description = "회원 가입을 진행합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse.Join>> joinMember(
        @RequestBody MemberRequest.Join dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(memberService.join(dto)));
    }

    @Operation(summary = "Reissue API", description = "Refresh Token 을 통해 AccessToken, RefreshToken 모두 재발급합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse.Create>> updateTokens(
        @RequestBody TokenRequest.Update dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.createSuccess(userDetailService.updateToken(dto.refreshToken())));
    }

    @Operation(summary = "로그인 API", description = "회원 로그인 진행 & JWT 발급")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse.Create>> login(
        @RequestBody MemberRequest.Login dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.createSuccess(userDetailService.login(dto)));
    }

}
