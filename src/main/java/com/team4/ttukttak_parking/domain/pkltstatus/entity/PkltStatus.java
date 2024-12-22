package com.team4.ttukttak_parking.domain.pkltstatus.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "pklt_status")
public class PkltStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkltStatusId;

    private int tpkct;          // 총 주차 면
    private int nowPrkVhclCnt;  // 현재 주차 차량 수

    @LastModifiedDate
    private LocalDateTime nowPrkVhclUpdtTm; // 현재 주차 차량 업데이트 시간

    public static PkltStatus to(JsonNode data) {
        return PkltStatus.builder()
            .tpkct(data.get("tpkct").asInt())
            .nowPrkVhclCnt(data.get("now_prk_vhcl_cnt").asInt())
            .build();
    }

    public void fixStreetPklt() {
        this.tpkct += 1;
    }

    public void updateNowPrkVhclCnt() {
        this.nowPrkVhclCnt += 1;
    }
}
