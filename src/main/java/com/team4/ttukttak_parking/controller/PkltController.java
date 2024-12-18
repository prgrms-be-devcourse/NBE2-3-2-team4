package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltInfoResponse;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.service.PkltService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/{pkltId}")
    public ResponseEntity<ApiResponse<PkltResponse.Read>> getParkingLots(@PathVariable Long pkltId) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getParkingLots(pkltId)));
    }


    @Operation(summary = "좌표 근처 주차장 정보 조회 API", description = "좌표 근처 주차장 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PkltResponse.ReadNearby>>> getParkingLots(
        @Parameter(description = "위도") @RequestParam(value = "lat") BigDecimal lat,
        @Parameter(description = "경도") @RequestParam(value = "lng") BigDecimal lng) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getNearbyParkingLots(lat, lng)));

    }

    @Operation(summary = "주차장 상세 정보 조회 API", description = "주차장 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/{pkltId}/info")
    public ResponseEntity<ApiResponse<PkltInfoResponse.Read>> getParkingLotInfo(
        @PathVariable Long pkltId) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(pkltService.getParkingLotInfo(pkltId)));
    }
}
