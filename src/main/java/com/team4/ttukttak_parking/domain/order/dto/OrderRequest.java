package com.team4.ttukttak_parking.domain.order.dto;

public record OrderRequest() {

    public record Order(
        Long ticketId,
        String carNumber
    ) {

    }
}
