package com.team4.ttukttak_parking.domain.pkltstatus.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

public interface PkltStatusRepository extends JpaRepository<PkltStatus, Long> {
    //주차장 고유 아이디로 주차장 상태 조회 Optional 로 타입을 지정해 기능 사용
    Optional<PkltStatus> findByPklt(Pklt pklt);
    //주차장의 아이디들을 받아서 존재한다면 주자상태 정보를 찾아 리스트로 담아둔다
    List<PkltStatus> findByPkltPkltIdIn(List<Long> pkltIds);

}
