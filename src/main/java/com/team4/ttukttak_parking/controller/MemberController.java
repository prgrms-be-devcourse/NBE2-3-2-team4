package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.member.service.MemberService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "😏 User", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입 API", description = "회원 가입을 진행합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<MemberResponse.Join>> joinUser(
        @RequestBody MemberRequest.Join dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(memberService.join(dto)));
    }

}
