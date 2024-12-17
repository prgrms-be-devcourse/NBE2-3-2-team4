package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.member.dto.MemberResponseDto;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.dto.MemberRequestDto;
import com.team4.ttukttak_parking.domain.member.service.MemberService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 관련 API")
public class MemberController {

    @Autowired
    private MemberService memberService;


    @Operation(summary = "회원가입 API", description = "회원 등록후 가입email,사용자 이름을 리턴합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<MemberResponseDto.Join>> joinUser(@RequestBody MemberRequestDto.Join dto ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.createSuccess( memberService.join(dto) ));
    }

}
