package com.team4.ttukttak_parking.domain.order.dto;

import com.team4.ttukttak_parking.domain.order.entity.Order;


public record OrderResponse(

) {

    public record CreateOrder(
        Long ticketId,
        Long memberId,
        String carNum
    ) {

        public static CreateOrder from(Long ticketId, Long memberId, String carNum) {
            return new CreateOrder(ticketId, memberId, carNum);
        }
    }

    public record GetOrder(
        Order order
    ) {

        public static GetOrder from(Order order) {
            return new GetOrder(order);
        }
    }
}
