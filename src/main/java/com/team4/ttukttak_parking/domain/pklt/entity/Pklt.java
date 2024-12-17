package com.team4.ttukttak_parking.domain.pklt.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pklt")
public class Pklt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkltId;

    private String pkltNm;
    private String addr;

    @Column(precision = 16, scale = 10, nullable = false)
    private BigDecimal lat;

    @Column(precision = 16, scale = 10, nullable = false)
    private BigDecimal lot;

    public static Pklt to(JsonNode data) {
        return Pklt.builder()
            .pkltNm(data.asText("pklt_nm"))
            .addr(data.get("addr").asText())
            .lat(BigDecimal.valueOf(data.get("lat").asDouble()))
            .lot(BigDecimal.valueOf(data.get("lot").asDouble()))
            .build();
    }

}
