package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PkltSimpleDto {
    Long id;
    BigDecimal latitude;
    BigDecimal longitude;


    public static PkltSimpleDto from(Pklt pklt){
        return new PkltSimpleDto(pklt.getPkltId(),pklt.getLat(),pklt.getLot());
    }
}
