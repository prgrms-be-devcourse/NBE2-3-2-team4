package com.team4.ttukttak_parking.domain.admin.repository;

import com.team4.ttukttak_parking.domain.admin.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
