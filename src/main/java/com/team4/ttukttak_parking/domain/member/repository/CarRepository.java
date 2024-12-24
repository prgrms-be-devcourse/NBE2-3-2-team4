package com.team4.ttukttak_parking.domain.member.repository;

import com.team4.ttukttak_parking.domain.member.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {

    List<Car> findByMemberEmail(String email);
}
