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


    @Operation(summary = "회원조회", description = "email을 입력받아 사용자 정보를 리턴합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/mypage/info")
    public ResponseEntity<ApiResponse<MemberResponse.Join>> getMemberInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok.body(
                ApiResponse.createSuccess(memberService.getMemberInfo(user.getUsername)));
    }


    @Operation(summary = "회원정보수정", description = "회원의 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PutMapping("/mypage/myinfo")
    public ResponseEntity<ApiResponse<Void>> modifyMember(@AuthenticationPrincipal User user,
            @RequestBody MemberRequest.Modify dto) {
        return ResponseEntity.ok().body(
                ApiResponse.createSuccess(memberService.modifyMember(dto)));
    }

}
