package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PkltInfoResponse {
    private Long pkltCd;
    private Long pkltId;
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
    public PkltInfoResponse(Long pkltCd, Long pkltId, String prkTypeNm, int wdOperBgngTm, int wdOperEndTm, int weOperBgngTm, int weOperEndTm, int lhldyOperBgngTm, int lhldyOperEndTm, int bscPrkCrg, int bscPrkHr, int addPrkCrg, int addPrkHr, int dayMaxCrg) {
        this.pkltCd = pkltCd;
        this.pkltId = pkltId;
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
