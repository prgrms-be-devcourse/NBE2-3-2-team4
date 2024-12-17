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

    @Builder
    public PkltResponse(Long pkltId, String pkltName, String address, BigDecimal latitude, BigDecimal longitude) {
        this.pkltId = pkltId;
        this.pkltName = pkltName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
