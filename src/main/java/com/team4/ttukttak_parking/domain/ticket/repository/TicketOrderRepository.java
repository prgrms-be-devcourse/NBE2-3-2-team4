package com.team4.ttukttak_parking.domain.ticket.repository;

import com.team4.ttukttak_parking.domain.ticket.entity.TicketOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketOrderRepository extends JpaRepository<TicketOrder, Long> {

}
