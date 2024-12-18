package com.team4.ttukttak_parking.domain.pkltstatus.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PkltStatusRepository extends JpaRepository<PkltStatus, Long> {
    //주차장 고유 아이디로 주차장 상태 조회 Optional 로 타입을 지정해 기능 사용
    Optional<PkltStatus> findByPklt(Pklt pklt);
    Optional<PkltStatus> findByPklt_PkltId(Long pkltId);
}
