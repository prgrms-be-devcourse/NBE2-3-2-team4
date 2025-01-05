package com.team4.ttukttak_parking.domain.ticket.dto;

import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;

public record TicketResponse(
        Long ticketId,
        int price,
        int pkDuration
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.getTicketId(), ticket.getPrice(), ticket.getPkDuration());
    }

}
