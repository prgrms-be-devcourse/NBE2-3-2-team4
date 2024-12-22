package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse.GetMember;
import com.team4.ttukttak_parking.domain.member.service.MemberService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "😏 Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "회원 조회 API", description = "회원 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<GetMember>> getMemberInfo(
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(
            ApiResponse.createSuccess(memberService.getMemberInfo(user.getUsername())));
    }


    @Operation(summary = "회원 정보 수정 API", description = "회원 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> modifyMember(@AuthenticationPrincipal User user,
        @RequestBody MemberRequest.Modify dto) {
        return ResponseEntity.ok().body(
            ApiResponse.createSuccess(memberService.modifyInfo(dto, user.getUsername())));
    }

}
