package com.team4.ttukttak_parking.domain.pklt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PkltStatusResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Read {
        private Long pkltId;
        private int availableSpots; // 잔여 자리 수
        private int usedSpots;      // 현재 사용 중인 자리 수
        private int totalSpots;     // 총 주차장 자리 수
    }


}
