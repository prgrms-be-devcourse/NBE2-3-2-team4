package com.team4.ttukttak_parking.domain.order.dto;

public record OrderRequest() {

    public record CreateOrder(
        Long ticketId,
        String carNumber
    ) {

    }
}
