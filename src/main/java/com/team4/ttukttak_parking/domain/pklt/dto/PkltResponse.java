package com.team4.ttukttak_parking.domain.pklt.dto;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PkltResponse {

    private Long pkltId;
    private String pkltName;
    private String address;
    private BigDecimal latitude ;
    private BigDecimal longitude;
    private int availableSpots; //잔여 자리 수
    private int usedSpots;//현재 사용 중인 자리 수
    private int totalSpots;//총 주차장 자리 수

    @Builder
    public PkltResponse(Long pkltId, String pkltName, String address, BigDecimal latitude, BigDecimal longitude, int availableSpots, int usedSpots, int totalSpots) {
        this.pkltId = pkltId;
        this.pkltName = pkltName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availableSpots = availableSpots;
        this.usedSpots = usedSpots;
        this.totalSpots = totalSpots;
    }
}
