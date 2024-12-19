package com.team4.ttukttak_parking.domain.pklt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PkltStatusDetailsResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Read {
        private Long pkltId;                // 주차장 ID
        private int availableSpots;         // 잔여 자리 수
        private int usedSpots;              // 현재 사용 중인 자리 수
        private int totalSpots;             // 총 주차장 자리 수
        private String carNum;              // 차량 번호
        private LocalDateTime startTime;    // 주차 시작 시간
    }

}
