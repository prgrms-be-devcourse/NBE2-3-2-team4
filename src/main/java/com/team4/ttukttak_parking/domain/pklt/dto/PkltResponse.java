package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import java.math.BigDecimal;


public record PkltResponse() {

    public record GetPklt(
        Long pkltId,
        String pkltName,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
    ) {

        public static GetPklt from(com.team4.ttukttak_parking.domain.pklt.entity.Pklt pklt) {
            return new GetPklt(
                pklt.getPkltId(), pklt.getPkltNm(), pklt.getAddr(), pklt.getLat(), pklt.getLot());
        }
    }

    public record GetNearbyPklt(
        Long pkltId,
        BigDecimal latitude,
        BigDecimal longitude,
        String status
    ) {

        public static GetNearbyPklt from(com.team4.ttukttak_parking.domain.pklt.entity.Pklt pklt,
            String status) {
            return new GetNearbyPklt(pklt.getPkltId(), pklt.getLat(), pklt.getLot(), status);
        }
    }

    public record GetPkltStatus(
        Long pkltStatusId,
        int availableSpots,
        int usedSpots,
        int totalSpots
    ) {

        public static GetPkltStatus from(PkltStatus pkltStatus, int availableSpots) {
            return new GetPkltStatus(pkltStatus.getPkltStatusId(), availableSpots,
                pkltStatus.getNowPrkVhclCnt(), pkltStatus.getTpkct());
        }
    }

    public record GetPkltInfo(
        Long pkltCd,
        String prkTypeNm,      // 주차장 종류명
        int wdOperBgngTm,      // 평일 운영 시작시간(HHMM)
        int wdOperEndTm,      // 평일 운영 종료시간(HHMM)
        int weOperBgngTm,      // 주말 운영 시작시간(HHMM)
        int weOperEndTm,      // 주말 운영 종료시간(HHMM)
        int lhldyOperBgngTm,    // 공휴일 운영 시작시간(HHMM)
        int lhldyOperEndTm,   // 공휴일 운영 종료시간(HHMM)
        int bscPrkCrg,       // 기본 주차 요금
        int bscPrkHr,         // 기본 주차 시간(분 단위)
        int addPrkCrg,       // 추가 단위 요금
        int addPrkHr        // 추가 단위 시간(분 단위)
    ) {

        public static GetPkltInfo from(PkltInfo pkltInfo) {
            return new GetPkltInfo(
                pkltInfo.getPkltCd(), pkltInfo.getPrkTypeNm(), pkltInfo.getWdOperBgngTm(),
                pkltInfo.getWdOperEndTm(), pkltInfo.getWeOperBgngTm(), pkltInfo.getWeOperEndTm(),
                pkltInfo.getLhldyOperBgngTm(), pkltInfo.getLhldyOperEndTm(),
                pkltInfo.getBscPrkCrg(),
                pkltInfo.getBscPrkHr(), pkltInfo.getAddPrkCrg(), pkltInfo.getAddPrkHr()
            );
        }
    }

    public record EnterPklt(
        String carNum,
        Long pkltId
    ) {

        public static EnterPklt from(String carNum, Long pkltId) {
            return new EnterPklt(carNum, pkltId);
        }
    }

}
