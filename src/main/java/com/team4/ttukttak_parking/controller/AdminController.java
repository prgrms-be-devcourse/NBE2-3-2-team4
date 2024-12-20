package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.admin.service.AdminService;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.ReadPkltAndStatus;
import com.team4.ttukttak_parking.domain.ticket.service.TicketService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "👩‍🔧 Admin", description = "관리자 권한 필요 API")
public class AdminController {

    private final AdminService adminService;
    private final TicketService ticketService;

    @Operation(summary = "기본 주차장 데이터 저장 API", description = "data.json 의 기본 데이터를 저장합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/data")
    public ResponseEntity<ApiResponse<Void>> saveParkingDataFromJson() throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(adminService.loadDefaultDataByJson()));
    }

    @Operation(summary = "주차장 목록 조회 API", description = "주차장 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/pklt")
    public ResponseEntity<ApiResponse<List<ReadPkltAndStatus>>> getPkltList() {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(adminService.getLists()));

    }

    @Operation(summary = "주차장 별 주차권 데이터 생성 API", description = "주차장 별로 주차권 데이터를 생성 후 저장합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/tickets")
    public ResponseEntity<ApiResponse<Void>> saveTicketsData() {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(ticketService.loadDefaultTickets())
        );
    }

}
