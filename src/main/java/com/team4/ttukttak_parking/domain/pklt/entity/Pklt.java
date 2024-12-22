package com.team4.ttukttak_parking.domain.pklt.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pkltStatusId")
    private PkltStatus pkltStatus;

    @OneToMany(mappedBy = "pklt", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @Column(precision = 16, scale = 10, nullable = false)
    private BigDecimal lat;

    @Column(precision = 16, scale = 10, nullable = false)
    private BigDecimal lot;

    public static Pklt to(JsonNode data) {
        return Pklt.builder()
            .pkltNm(data.get("pklt_nm").asText())
            .addr(data.get("addr").asText())
            .lat(BigDecimal.valueOf(data.get("lat").asDouble()))
            .lot(BigDecimal.valueOf(data.get("lot").asDouble()))
            .build();
    }

}
