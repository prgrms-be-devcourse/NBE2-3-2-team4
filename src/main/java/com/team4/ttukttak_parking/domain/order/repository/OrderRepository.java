package com.team4.ttukttak_parking.domain.order.repository;

import com.team4.ttukttak_parking.domain.order.dto.OrderResponse;
import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByCarNumAndAndStatus(String carNum, ParkingStatus status);
    Optional<Order> findByCarNumAndStatus(String carNum, ParkingStatus status);

    @Query("select d.pkltStatusDetailId, o.carNum, t.price, p.pkltNm ,d.startTime,d.endTime " +
            "from Order o " +
            "inner join o.ticket t " +
            "inner join o.statusDetail d " +
            "inner join t.pklt p " +
            "where o.member.email = :email")
    Optional<List<Object[]>> findOrderList(@Param("email") String email);

}
