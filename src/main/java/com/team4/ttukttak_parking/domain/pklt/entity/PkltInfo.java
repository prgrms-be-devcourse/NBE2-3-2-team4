package com.team4.ttukttak_parking.domain.pklt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pklt_info")
public class PkltInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkltCd;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pklt_id", nullable = false)
    private Pklt pklt;

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
}
