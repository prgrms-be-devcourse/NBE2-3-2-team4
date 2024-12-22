package com.team4.ttukttak_parking.domain.ticket.repository;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByPriceAndPkDuration(int price, int pkDuration);


    List<Ticket> findByPklt(Pklt pklt);
}
