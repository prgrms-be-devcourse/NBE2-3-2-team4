package com.team4.ttukttak_parking.domain.ticket.dto;

import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import java.time.LocalDateTime;

public record TicketResponseDto() {

    public record Create(
        int price,
        int pkDuration,
        LocalDateTime createdAt
    ) {

        public static Create from(Ticket ticket) {
            return new Create(ticket.getPrice(), ticket.getPkDuration(), ticket.getCreatedAt());
        }
    }
}
