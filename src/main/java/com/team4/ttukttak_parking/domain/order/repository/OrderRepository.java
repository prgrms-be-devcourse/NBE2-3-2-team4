package com.team4.ttukttak_parking.domain.order.repository;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCarNumAndStatus(String carNum, ParkingStatus status);
}
