package com.team4.ttukttak_parking.domain.ticket.service;

import com.team4.ttukttak_parking.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

}
