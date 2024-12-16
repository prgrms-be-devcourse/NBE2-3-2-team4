package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.service.PkltService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parking")
@Slf4j
public class PkltController {

    private final PkltService pkltService;

    @GetMapping("/{pkltId}")
    public ResponseEntity<ApiResponse<PkltResponse>> getParkingLots(@PathVariable Long pkltId) {
        return ResponseEntity.ok().body(ApiResponse.createSuccess(pkltService.getParkingLots(pkltId)));
    }
}
