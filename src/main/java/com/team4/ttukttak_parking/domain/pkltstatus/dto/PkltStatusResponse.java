package com.team4.ttukttak_parking.domain.pkltstatus.dto;

import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;

public record PkltStatusResponse() {

    public record Read(
        Long pkltStatusId,
        int availableSpots,
        int usedSpots,
        int totalSpots
    ) {

        public static Read from(PkltStatus pkltStatus, int availableSpots) {
            return new Read(pkltStatus.getPkltStatusId(), availableSpots,
                pkltStatus.getNowPrkVhclCnt(), pkltStatus.getTpkct());
        }
    }


}
