package com.team4.ttukttak_parking.domain.pklt.service;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltInfoResponse;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltStatusDetailsResponse;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltStatusResponse;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusDetailRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PkltService {

    private final PkltRepository pkltRepository;
    private final PkltInfoRepository pkltInfoRepository;
    private final PkltStatusRepository pkltStatusRepository;
    private final PkltStatusDetailRepository pkltStatusDetailRepository;

    @Transactional(readOnly = true)
    public PkltResponse.Read getParkingLots(Long pkltId) {
        Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltResponse.Read.builder()
            .pkltId(pklt.getPkltId())
            .pkltName(pklt.getPkltNm())
            .address(pklt.getAddr())
            .latitude(pklt.getLat())
            .longitude(pklt.getLot())
            .build();
    }

    @Transactional(readOnly = true)
    public PkltInfoResponse.Read getParkingLotInfo(Long pkltId) {
        PkltInfo pkltInfo = pkltInfoRepository.findByPklt_PkltId(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltInfoResponse.Read.builder()
            .pkltCd(pkltInfo.getPkltCd())
            .prkTypeNm(pkltInfo.getPrkTypeNm())
            .wdOperBgngTm(pkltInfo.getWdOperBgngTm())
            .wdOperEndTm(pkltInfo.getWdOperEndTm())
            .weOperBgngTm(pkltInfo.getWeOperBgngTm())
            .weOperEndTm(pkltInfo.getWeOperEndTm())
            .lhldyOperBgngTm(pkltInfo.getLhldyOperBgngTm())
            .lhldyOperEndTm(pkltInfo.getLhldyOperEndTm())
            .bscPrkCrg(pkltInfo.getBscPrkCrg())
            .bscPrkHr(pkltInfo.getBscPrkHr())
            .addPrkCrg(pkltInfo.getAddPrkCrg())
            .addPrkHr(pkltInfo.getAddPrkHr())
            .dayMaxCrg(pkltInfo.getDayMaxCrg())
            .build();
    }

    @Transactional(readOnly = true)
    public List<PkltResponse.ReadNearby> getNearbyParkingLots(BigDecimal lat, BigDecimal lng) {
        double KM = 10;
        double lngDifference = KM / 111 / (Math.cos(lat.doubleValue()));

        Predicate<Pklt> latFilter = pklt ->
            pklt.getLat().doubleValue() > lat.doubleValue() - (KM / 111)
                && lat.doubleValue() + (KM / 111) > pklt.getLat().doubleValue();
        Predicate<Pklt> lngFilter = pklt ->
            pklt.getLot().doubleValue() > lng.doubleValue() - lngDifference
                && pklt.getLot().doubleValue() < lng.doubleValue() + lngDifference;

        return pkltRepository.findAll().stream().filter(latFilter.and(lngFilter))
            .map(PkltResponse.ReadNearby::from).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public PkltStatusResponse.Read getParkingLotsStatus(Long pkltId) {
        Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        PkltStatus pkltStatus = pkltStatusRepository.findByPklt(pklt)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        int availableSpots = pkltStatus.getTpkct() - pkltStatus.getNowPrkVhclCnt();

        return PkltStatusResponse.Read.builder()
            .pkltId(pklt.getPkltId())
            .availableSpots(availableSpots)
            .usedSpots(pkltStatus.getNowPrkVhclCnt())
            .totalSpots(pkltStatus.getTpkct())
            .build();
    }

    @Transactional
    public PkltStatusDetailsResponse.Read reserveGuestParking(Long pkltId, String carNum) {
        PkltStatus pkltStatus = pkltStatusRepository.findByPklt_PkltId(pkltId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        if (pkltStatus.getNowPrkVhclCnt() >= pkltStatus.getTpkct()) {
            throw new NotFoundException(ErrorCode.PKLT_FULL);
        }

        if (pkltStatusDetailRepository.existsByCarNumAndStatusIn(carNum, Arrays.asList(ParkingStatus.WAITING, ParkingStatus.PARKING))) {
            throw new NotFoundException(ErrorCode.PKLT_ALREADY_PARKED);
        }

        pkltStatus.setNowPrkVhclCnt(pkltStatus.getNowPrkVhclCnt() + 1);

        PkltStatusDetail pkltStatusDetail = PkltStatusDetail.to(pkltStatus.getPklt(), carNum, ParkingStatus.PARKING);
        pkltStatusDetailRepository.save(pkltStatusDetail);

        return PkltStatusDetailsResponse.Read.builder()
                .pkltId(pkltStatus.getPklt().getPkltId())
                .availableSpots(pkltStatus.getTpkct() - pkltStatus.getNowPrkVhclCnt())
                .usedSpots(pkltStatus.getNowPrkVhclCnt())
                .totalSpots(pkltStatus.getTpkct())
                .carNum(carNum)
                .startTime(pkltStatusDetail.getStartTime())
                .build();
    }
}
