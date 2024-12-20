package com.team4.ttukttak_parking.domain.pklt.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PkltInfoRepository extends JpaRepository<PkltInfo, Long> {
    Optional<PkltInfo> findByPklt_PkltId(Long pkltId);

    Optional<PkltInfo> findByPklt(Pklt pklt);
}
