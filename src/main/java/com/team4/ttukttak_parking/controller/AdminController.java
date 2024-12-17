package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.admin.service.AdminService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "👩‍🔧 Admin", description = "관리자 관련 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "기본 데이터 호출 API", description = "data.json 의 기본 데이터를 호출합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/json")
    public ResponseEntity<ApiResponse<Void>> saveParkingDataFromJson() throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(adminService.loadDefaultDataByJson()));
    }

}
