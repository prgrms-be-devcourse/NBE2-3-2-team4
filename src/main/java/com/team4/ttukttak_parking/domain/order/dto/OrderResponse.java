package com.team4.ttukttak_parking.domain.order.dto;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import java.time.LocalDateTime;


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
        String pkltNm,
        String carNum,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int pkDuration,
        int price,
        int addPkDuration,
        int addPrice,
        int totalPrice
    ) {

        public static GetOrder from(Pklt pklt, Order order, PkltStatusDetail statusDetail,
            Ticket ticket, int addPkDuration, int addPrice) {
            return new GetOrder(pklt.getPkltNm(), order.getCarNum(), statusDetail.getStartTime(),
                statusDetail.getEndTime(), ticket.getPkDuration(), ticket.getPrice(), addPkDuration,
                addPrice, ticket.getPrice() + addPrice);
        }
    }
}
