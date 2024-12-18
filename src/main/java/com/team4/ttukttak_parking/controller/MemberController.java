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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<MemberResponse.Join>> joinMember(
        @RequestBody MemberRequest.Join dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(memberService.join(dto)));
    }


    @Operation(summary = "회원조회", description = "email을 입력받아 사용자 정보를 리턴합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/memberInfo")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberInfo(
            @RequestParam String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.createSuccess(memberService.getMemberInfo(email)));
    }


    @Operation(summary = "회원정보수정", description = "수정정보를 입력받고 변경된 사용자 전체 정보 리턴")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/memberModify")
    public ResponseEntity<ApiResponse<MemberResponse>> modifyMember(
            @RequestBody MemberRequest.Modify modifyInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.createSuccess(memberService.modifyMember(modifyInfo)));
    }


}
