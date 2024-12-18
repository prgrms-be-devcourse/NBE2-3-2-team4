package com.team4.ttukttak_parking.domain.ticket.service;

import com.team4.ttukttak_parking.domain.ticket.dto.TicketRequestDto;
import com.team4.ttukttak_parking.domain.ticket.dto.TicketResponseDto;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import com.team4.ttukttak_parking.domain.ticket.repository.TicketRepository;
import com.team4.ttukttak_parking.global.exception.BadRequestException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public TicketResponseDto.Create createTicket(TicketRequestDto.Create dto) {
        if (ticketRepository.findByPriceAndPkDuration(dto.price(), dto.pkDuration()).isPresent()) {
            throw new BadRequestException(ErrorCode.TICKET_ALREADY_EXIST);
        }
        
        return TicketResponseDto.Create.from(ticketRepository.save(Ticket.to(dto)));
    }
}
