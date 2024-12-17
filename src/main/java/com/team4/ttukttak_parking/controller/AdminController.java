package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.admin.service.AdminService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
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
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/json")
    public ResponseEntity<ApiResponse<Void>> saveParkingDataFromJson() throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.createSuccess(adminService.loadDefaultDataByJson()));
    }

}
