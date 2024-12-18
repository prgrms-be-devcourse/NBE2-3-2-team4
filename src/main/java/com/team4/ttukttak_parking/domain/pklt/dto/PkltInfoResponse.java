package com.team4.ttukttak_parking.domain.pklt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class PkltInfoResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Read {

        private Long pkltCd;
        private String prkTypeNm;       // 주차장 종류명
        private int wdOperBgngTm;       // 평일 운영 시작시간(HHMM)
        private int wdOperEndTm;        // 평일 운영 종료시간(HHMM)
        private int weOperBgngTm;       // 주말 운영 시작시간(HHMM)
        private int weOperEndTm;        // 주말 운영 종료시간(HHMM)
        private int lhldyOperBgngTm;    // 공휴일 운영 시작시간(HHMM)
        private int lhldyOperEndTm;     // 공휴일 운영 종료시간(HHMM)
        private int bscPrkCrg;          // 기본 주차 요금
        private int bscPrkHr;           // 기본 주차 시간(분 단위)
        private int addPrkCrg;          // 추가 단위 요금
        private int addPrkHr;           // 추가 단위 시간(분 단위)
        private int dayMaxCrg;          // 일 최대 요금

        @Builder
        public Read(Long pkltCd, String prkTypeNm, int wdOperBgngTm, int wdOperEndTm,
            int weOperBgngTm, int weOperEndTm, int lhldyOperBgngTm, int lhldyOperEndTm,
            int bscPrkCrg, int bscPrkHr, int addPrkCrg, int addPrkHr, int dayMaxCrg) {
            this.pkltCd = pkltCd;
            this.prkTypeNm = prkTypeNm;
            this.wdOperBgngTm = wdOperBgngTm;
            this.wdOperEndTm = wdOperEndTm;
            this.weOperBgngTm = weOperBgngTm;
            this.weOperEndTm = weOperEndTm;
            this.lhldyOperBgngTm = lhldyOperBgngTm;
            this.lhldyOperEndTm = lhldyOperEndTm;
            this.bscPrkCrg = bscPrkCrg;
            this.bscPrkHr = bscPrkHr;
            this.addPrkCrg = addPrkCrg;
            this.addPrkHr = addPrkHr;
            this.dayMaxCrg = dayMaxCrg;
        }
    }
}
