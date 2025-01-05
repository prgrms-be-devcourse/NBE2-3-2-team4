package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.kakao.dto.KakaoResponse;
import com.team4.ttukttak_parking.domain.kakao.service.KaKaoService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KaKaoController {

    private final KaKaoService kaKaoService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<KakaoResponse.Place>>> searchPlace(
        @Parameter(description = "검색 키워드") @RequestParam(value = "keyword") String keyword,
        @Parameter(description = "한 페이지 당 항목 개수(최대 45개)") @RequestParam(value = "size") int size) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(kaKaoService.searchPlace(keyword, size)));
    }
}
