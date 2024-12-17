package com.team4.ttukttak_parking.domain.pklt.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pklt_info")
public class PkltInfo {

    @Id
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

    public static PkltInfo toEntity(JsonNode data, Pklt pklt) {
        return PkltInfo.builder()
            .pkltCd(data.get("pklt_cd").asLong())
            .pklt(pklt)
            .prkTypeNm(data.get("prk_type_nm").asText())
            .wdOperBgngTm(data.get("wd_oper_bgng_tm").asInt())
            .wdOperEndTm(data.get("wd_oper_end_tm").asInt())
            .weOperBgngTm(data.get("we_oper_bgng_tm").asInt())
            .wdOperEndTm(data.get("we_oper_end_tm").asInt())
            .lhldyOperBgngTm(data.get("lhldy_oper_bgng_tm").asInt())
            .lhldyOperEndTm(data.get("lhldy_oper_end_tm").asInt())
            .bscPrkCrg(data.get("bsc_prk_crg").asInt())
            .bscPrkHr(data.get("bsc_prk_hr").asInt())
            .addPrkCrg(data.get("add_prk_crg").asInt())
            .addPrkHr(data.get("add_prk_hr").asInt())
            .dayMaxCrg(data.get("day_max_crg").asInt())
            .build();
    }
}
