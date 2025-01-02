package com.team4.ttukttak_parking.domain.pklt.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PkltRepository extends JpaRepository<Pklt, Long> {

    Pklt findByPkltNm(String pkltNm);
}
