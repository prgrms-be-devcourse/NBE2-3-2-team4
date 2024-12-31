package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.member.dto.MemberCarResponseDto;
import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse.GetMember;
import com.team4.ttukttak_parking.domain.member.service.MemberService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "등록된 차량 조회 API", description = "회원의 등록된 차량 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @GetMapping("/cars")
    public ResponseEntity<ApiResponse<List<MemberCarResponseDto>>> getMembersCars(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(
                ApiResponse.createSuccess(memberService.getMembersCars(user.getUsername())));
    }

    @Operation(summary = "차량 등록 API", description = "차량 정보를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/cars")
    public ResponseEntity<ApiResponse<Object>> registerCar(@AuthenticationPrincipal User user,@RequestBody CarSimpleDto carSimpleDto){
        memberService.registerCar(user.getUsername(),carSimpleDto.getCarNum());
        return ResponseEntity.ok().body(
                ApiResponse.createSuccessWithNoData());
    }

    @Operation(summary = "대표 차량 수정 API", description = "대표 차량 등록 또는 해제를 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PutMapping("/cars/{id}")
    public ResponseEntity<ApiResponse<Object>> registerPrimary(@AuthenticationPrincipal User user,@PathVariable Long id ,@RequestParam(value = "status") String status ){
        memberService.updatePrimary(user.getUsername(),id, status);
        return ResponseEntity.ok().body(
                ApiResponse.createSuccessWithNoData());
    }
    @Data
    static class CarSimpleDto{
        String carNum;
    }

}
