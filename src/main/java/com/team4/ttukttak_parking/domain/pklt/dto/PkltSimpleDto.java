package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PkltSimpleDto {

    Long id;
    BigDecimal latitude;
    BigDecimal longitude;

    public static PkltSimpleDto from(Pklt pklt) {
        return new PkltSimpleDto(pklt.getPkltId(), pklt.getLat(), pklt.getLot());
    }
}
