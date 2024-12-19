package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.member.dto.MemberDto;
import com.team4.ttukttak_parking.domain.member.dto.MemberModifyRequestDto;
import com.team4.ttukttak_parking.domain.member.service.MemberService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "😏 User", description = "인증/인가를 제외한 회원 관련 API")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "회원조회", description = "email을 입력받아 사용자 정보를 리턴합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/mypage/myinfo")
    public ResponseEntity<ApiResponse<MemberDto>> getMemberInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.createSuccess(memberService.getMemberInfo(user.getUsername())));
    }


    @Operation(summary = "회원정보수정", description = "수정정보를 입력받고 변경된 사용자 전체 정보 리턴")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PutMapping("/mypage/myinfo")
    public ResponseEntity<ApiResponse<MemberDto>> modifyMember(@AuthenticationPrincipal User user,
            @RequestBody MemberModifyRequestDto memberDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.createSuccess(memberService.modifyMember(user.getUsername(),memberDto)));
    }





}
