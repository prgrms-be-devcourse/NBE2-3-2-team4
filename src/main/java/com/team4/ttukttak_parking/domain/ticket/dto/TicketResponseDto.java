package com.team4.ttukttak_parking.domain.ticket.dto;

import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;

public record TicketResponseDto() {



    public record Read(
        Long ticketId,
        int price,
        int pkDuration

    ){

        public static Read from(Ticket ticket) {
            return new Read(
                  ticket.getTicketId(),ticket.getPrice(),ticket.getPkDuration());
        }
    }

}
