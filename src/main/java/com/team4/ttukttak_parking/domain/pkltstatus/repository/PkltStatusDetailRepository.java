package com.team4.ttukttak_parking.domain.pkltstatus.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PkltStatusDetailRepository extends JpaRepository<PkltStatusDetail, Long> {
    boolean existsByCarNumAndStatusIn(String carNum, List<ParkingStatus> statuses);
}
