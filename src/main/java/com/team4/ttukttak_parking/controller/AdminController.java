package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.admin.service.AdminService;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.ReadPkltAndStatus;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "👩‍🔧 Admin", description = "관리자 관련 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "기본 데이터 저장 API", description = "data.json 의 기본 데이터를 저장합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/json")
    public ResponseEntity<ApiResponse<Void>> saveParkingDataFromJson() throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(adminService.loadDefaultDataByJson()));
    }

    @Operation(summary = "주차장 목록 조회 API", description = "주차장 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/pklt")
    public ResponseEntity<ApiResponse<List<ReadPkltAndStatus>>> getPkltAllList() {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.createSuccess(adminService.getLists()));

    }

}
