package com.team4.ttukttak_parking.domain.order.repository;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
