package com.team4.ttukttak_parking.domain.pkltstatus.service;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.dto.PkltStatusResponse;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PkltStatusService {

    private final PkltRepository pkltRepository;
    private final PkltStatusRepository pkltStatusRepository;

    @Transactional(readOnly = true)
    public PkltStatusResponse.Read getPkltStatus(Long pkltId) {
        Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        PkltStatus pkltStatus = pkltStatusRepository.findByPklt(pklt)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        int availableSpots = pkltStatus.getTpkct() - pkltStatus.getNowPrkVhclCnt();

        return PkltStatusResponse.Read.from(pkltStatus, availableSpots);
    }
}
