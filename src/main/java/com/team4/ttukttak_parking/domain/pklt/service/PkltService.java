package com.team4.ttukttak_parking.domain.pklt.service;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
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
    private final PkltStatusRepository pkltStatusRepository;

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

    public PkltResponse getParkingLotsStatus(Long pkltId) {
        //주차장 조회 -> 없을시 NOT FOUND
        Pklt pklt=pkltRepository.findById(pkltId)
                .orElseThrow(()->new NotFoundException(ErrorCode.PKLT_NOT_FOUND));
        //주차장 상태 정보 조회->없을시 NOT FOUND
        PkltStatus pkltStatus=pkltStatusRepository.findByPkltStatusId(pkltId)
                .orElseThrow(()->new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        //총 면-현재 주차 대수
        int availableSpots=pkltStatus.getTpkct()-pkltStatus.getNowPrkVhclCnt();//

        return PkltResponse.builder()
                .pkltId(pklt.getPkltId())
                .pkltName(pklt.getPkltNm())
                .address(pklt.getAddr())
                .latitude(pklt.getLat())
                .longitude(pklt.getLot())
                .totalSpots(pkltStatus.getTpkct())
                .usedSpots(pkltStatus.getNowPrkVhclCnt())
                .availableSpots(availableSpots)
                .build();
    }
}
