package com.team4.ttukttak_parking.domain.ticket.dto;

public record TicketRequestDto() {

    public record Create(
        int price,
        int pkDuration
    ){}
}
