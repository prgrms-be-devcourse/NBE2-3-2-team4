package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.admin.service.AdminService;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.ticket.dto.TicketRequestDto;
import com.team4.ttukttak_parking.domain.ticket.dto.TicketResponseDto;
import com.team4.ttukttak_parking.domain.ticket.service.TicketService;
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
    private final TicketService ticketService;

    @Operation(summary = "기본 데이터 저장 API", description = "data.json 의 기본 데이터를 저장합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/json")
    public ResponseEntity<ApiResponse<Void>> saveParkingDataFromJson() throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(adminService.loadDefaultDataByJson()));
    }

    @Operation(summary = "주차권 상품 등록 API", description = "주차권 상품을 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("/tickets")
    public ResponseEntity<ApiResponse<TicketResponseDto.Create>> regTicket(
        @RequestBody TicketRequestDto.Create dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(ticketService.createTicket(dto)));
    }
    @Operation(summary = "주차장 목록 조회 API", description = "주차장 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/pklt")
    public ResponseEntity<List<PkltResponse.GetList>> getPkltAllList() {
        return ResponseEntity.ok().body(adminService.getLists());

    }

}
