package com.team4.ttukttak_parking.domain.pklt.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PkltRepository extends JpaRepository<Pklt, Long> {
    //리스트
    List<Pklt> findAll();

}
