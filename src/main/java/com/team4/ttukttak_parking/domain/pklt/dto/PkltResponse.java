package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import java.math.BigDecimal;


public record PkltResponse() {

    public record Read(
        Long pkltId,
        String pkltName,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
    ) {

        public static Read from(Pklt pklt) {
            return new Read(
                pklt.getPkltId(), pklt.getPkltNm(), pklt.getAddr(), pklt.getLat(), pklt.getLot());
        }
    }


    public record ReadNearby(
        Long id,
        BigDecimal latitude,
        BigDecimal longitude
    ) {

        public static ReadNearby from(Pklt pklt) {
            return new ReadNearby(pklt.getPkltId(), pklt.getLat(), pklt.getLot());
        }
    }


    public record ReadPkltAndStatus(
        Long pkltId,
        String pkltName,
        String address,
        int availableSpots,
        int usedSpots,
        int totalSpots
    ) {

        public static ReadPkltAndStatus from(Pklt pklt, int availableSpots, PkltStatus pkltStatus) {
            return new ReadPkltAndStatus(
                pklt.getPkltId(), pklt.getPkltNm(), pklt.getPkltNm(), availableSpots,
                pkltStatus.getNowPrkVhclCnt(), pkltStatus.getTpkct()
            );
        }
    }

}
