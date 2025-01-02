package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.GetNearbyPklt;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.GetPklt;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.GetPkltInfo;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.GetPkltStatus;
import com.team4.ttukttak_parking.domain.pklt.service.PkltService;
import com.team4.ttukttak_parking.domain.ticket.dto.TicketResponse;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pklt")
@Slf4j
@Tag(name = "🚗 Parking Lot", description = "주차장 관련 API")
public class PkltController {

    private final PkltService pkltService;

    @Operation(summary = "주차장 정보 조회 API", description = "주차장 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/{pkltId}")
    public ResponseEntity<ApiResponse<GetPklt>> getPklt(
        @PathVariable Long pkltId) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getPklt(pkltId)));
    }

    @Operation(summary = "좌표 근처 주차장 정보 조회 API", description = "좌표 근처 주차장 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<GetNearbyPklt>>> getNearbyPklt(
        @Parameter(description = "위도") @RequestParam(value = "lat") BigDecimal lat,
        @Parameter(description = "경도") @RequestParam(value = "lng") BigDecimal lng) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getNearbyPklt(lat, lng)));

    }

    @Operation(summary = "주차장 상세 정보 조회 API", description = "주차장 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/{pkltId}/info")
    public ResponseEntity<ApiResponse<GetPkltInfo>> getPkltInfo(
        @PathVariable Long pkltId) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getPkltInfo(pkltId)));
    }

    @Operation(summary = "주차장 잔여 자리 조회 API", description = "주차장 잔여 자리를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/{pkltId}/status")
    public ResponseEntity<ApiResponse<GetPkltStatus>> getPkltStatus(
        @PathVariable Long pkltId) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getPkltStatus(pkltId)));
    }

    @Operation(summary = "주차장 입차 API", description = "주차장에 입차합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @PostMapping("/{pkltId}/enter")
    public ResponseEntity<ApiResponse<PkltResponse.EnterPklt>> enterPklt(
        @Parameter(description = "차량 번호") @RequestParam(value = "carNum") String carNum,
        @PathVariable Long pkltId) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.createSuccess(pkltService.enterPklt(carNum, pkltId)));
    }

    @Operation(summary = "주차장 출차 API", description = "주차장에서 출차합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @PostMapping("/{pkltId}/exit")
    public ResponseEntity<ApiResponse<PkltResponse.ExitPklt>> exitPklt(
        @Parameter(description = "차량 번호") @RequestParam(value = "carNum") String carNum,
        @PathVariable Long pkltId) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.exitPklt(carNum, pkltId)));
    }

    @Operation(summary = "주차장별 주차권 리스트 조회", description = "주차장별 주차권 리스트를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/{pkltId}/tickets")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getPkltTickets(
            @PathVariable Long pkltId) {
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(pkltService.getPkltTicketList(pkltId)));
    }



    @Operation(summary = "주차권 결제 정보 확인", description = "주차권 결제 전에 주차권 결제 정보 확인합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/api/pklt/{pkltId}/ticket/{ticketId}")
    public ResponseEntity<ApiResponse<PkltResponse.PKltTicketDetail>> getPkltTicketsDetail(
            @PathVariable Long pkltId,
            @PathVariable Long ticketId,
            @RequestParam Long memberId) {
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(pkltService.getPKltTicketDetail(pkltId, ticketId, memberId)));
    }
}
