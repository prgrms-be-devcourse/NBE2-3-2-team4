package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class PkltResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Read {
        private Long pkltId;
        private String pkltName;
        private String address;
        private BigDecimal latitude ;
        private BigDecimal longitude;

        @Builder
        public Read(Long pkltId, String pkltName, String address, BigDecimal latitude, BigDecimal longitude) {
            this.pkltId = pkltId;
            this.pkltName = pkltName;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ReadNearby {
        Long id;
        BigDecimal latitude;
        BigDecimal longitude;

        public static ReadNearby from(Pklt pklt){
            return new ReadNearby(pklt.getPkltId(),pklt.getLat(),pklt.getLot());
        }
    }

}
