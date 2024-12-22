package com.team4.ttukttak_parking.domain.pklt.service;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltInfoResponse;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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

    @Transactional(readOnly = true)
    public PkltResponse.Read getPklt(Long pkltId) {
        Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltResponse.Read.from(pklt);
    }

    @Transactional(readOnly = true)
    public PkltInfoResponse.Read getPkltInfo(Long pkltId) {
        PkltInfo pkltInfo = pkltInfoRepository.findByPklt_PkltId(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltInfoResponse.Read.from(pkltInfo);
    }

    @Transactional(readOnly = true)
    public List<PkltResponse.ReadNearby> getNearbyPklt(BigDecimal lat, BigDecimal lng) {
        double KM = 0.3;
        double lngDifference = KM / 111 / (Math.cos(lat.doubleValue()));

        Predicate<Pklt> latFilter = pklt ->
            pklt.getLat().doubleValue() > lat.doubleValue() - (KM / 111)
                && lat.doubleValue() + (KM / 111) > pklt.getLat().doubleValue();
        Predicate<Pklt> lngFilter = pklt ->
            pklt.getLot().doubleValue() > lng.doubleValue() - lngDifference
                && pklt.getLot().doubleValue() < lng.doubleValue() + lngDifference;

        // 필터링된 주차장 리스트
        List<Pklt> nearbyPkltList = pkltRepository.findAll().stream()
            .filter(latFilter.and(lngFilter)).toList();

        // 혼잡도 계산 및 정렬을 위한 pkltId 목록 생성
        List<Long> pkltIds = nearbyPkltList.stream().map(Pklt::getPkltId).toList();

        // 혼잡도 정보를 가져오기 위한 pkltStatus 조회
        List<PkltStatus> pkltStatusList = pkltStatusRepository.findByPkltPkltIdIn(pkltIds);

        // 혼잡도 기준으로 정렬 및 전체 항목 반환
        return pkltStatusList.stream().sorted(
                Comparator.comparingDouble(
                    pkltStatus -> (double) pkltStatus.getNowPrkVhclCnt() / pkltStatus.getTpkct() * 100))
            .map(pkltStatus -> {
                double percentage =
                    (double) pkltStatus.getNowPrkVhclCnt() / pkltStatus.getTpkct() * 100;
                String status;
                if (percentage <= 30) {
                    status = "여유";
                } else if (percentage <= 70) {
                    status = "보통";
                } else {
                    status = "혼잡";
                }
                return PkltResponse.ReadNearby.from(pkltStatus.getPklt(), status);
            }).toList();
    }


}
