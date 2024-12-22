package com.team4.ttukttak_parking.domain.order.dto;

public record OrderResponse() {

    public record CreateOrder(
        Long ticketId,
        Long memberId,
        String carNum
    ) {

        public static CreateOrder from(Long ticketId, Long memberId, String carNum) {
            return new CreateOrder(ticketId, memberId, carNum);
        }
    }

}
