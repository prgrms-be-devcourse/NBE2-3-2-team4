package com.team4.ttukttak_parking.domain.tosspayment.repository;

import com.team4.ttukttak_parking.domain.tosspayment.entity.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {
}
