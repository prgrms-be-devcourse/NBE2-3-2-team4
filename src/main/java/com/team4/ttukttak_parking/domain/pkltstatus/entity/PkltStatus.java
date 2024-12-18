package com.team4.ttukttak_parking.domain.pkltstatus.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pklt_id", nullable = false)
    private Pklt pklt;

    private int tpkct;          // 총 주차 면
    private int nowPrkVhclCnt;  // 현재 주차 차량 수

    @LastModifiedDate
    private LocalDateTime nowPrkVhclUpdtTm; // 현재 주차 차량 업데이트 시간

    public static PkltStatus to(JsonNode data, Pklt pklt) {
        return PkltStatus.builder()
            .pklt(pklt)
            .tpkct(data.get("tpkct").asInt())
            .nowPrkVhclCnt(data.get("now_prk_vhcl_cnt").asInt())
            .build();
    }
}
