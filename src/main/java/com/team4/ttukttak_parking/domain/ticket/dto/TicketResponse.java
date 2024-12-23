package com.team4.ttukttak_parking.domain.ticket.dto;

import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;

public record TicketResponse(
    int price,
    int pkDuration
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.getPrice(), ticket.getPkDuration());
    }

}
