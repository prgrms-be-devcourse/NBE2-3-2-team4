package com.team4.ttukttak_parking.domain.ticket.repository;

import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
