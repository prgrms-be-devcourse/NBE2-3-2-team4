package com.team4.ttukttak_parking.domain.pklt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
}
