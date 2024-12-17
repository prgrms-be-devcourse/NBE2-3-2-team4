package com.team4.ttukttak_parking.domain.pklt.service;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PkltService {

    private final PkltRepository pkltRepository;

    public PkltResponse getParkingLots(Long pkltId) {
        Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltResponse.builder()
            .pkltId(pklt.getPkltId())
            .pkltName(pklt.getPkltNm())
            .address(pklt.getAddr())
            .latitude(pklt.getLat())
            .longitude(pklt.getLot())
            .build();
    }
}
