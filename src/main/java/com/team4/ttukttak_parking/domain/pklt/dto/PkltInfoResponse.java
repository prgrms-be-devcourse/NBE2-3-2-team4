package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record PkltInfoResponse() {

    public record Read(
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

        public static Read from(PkltInfo pkltInfo) {
            return new Read(
                pkltInfo.getPkltCd(), pkltInfo.getPrkTypeNm(), pkltInfo.getWdOperBgngTm(),
                pkltInfo.getWdOperEndTm(), pkltInfo.getWeOperBgngTm(), pkltInfo.getWeOperEndTm(),
                pkltInfo.getLhldyOperBgngTm(), pkltInfo.getLhldyOperEndTm(), pkltInfo.getBscPrkCrg(),
                pkltInfo.getBscPrkHr(), pkltInfo.getAddPrkCrg(), pkltInfo.getAddPrkHr()
            );
        }
    }
}
