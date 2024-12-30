package com.team4.ttukttak_parking.domain.order.dto;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
        String addr,
        String carNum,
        String startTime,
        String endTime,
        int pkDuration,
        int price,
        int addPkDuration,
        int addPrice,
        int totalPrice
    ) {
        public static GetOrder from(Pklt pklt, Order order, PkltStatusDetail statusDetail,
            Ticket ticket, int addPkDuration, int addPrice) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a h시 mm분");
            String start = statusDetail.getStartTime().format(formatter);
            String end = null;
            if (statusDetail.getEndTime() != null) {
                end = statusDetail.getEndTime().format(formatter);
            } else {
                end = "주차중";
            }
            return new GetOrder(pklt.getPkltNm(), pklt.getAddr(), order.getCarNum(), start,
                    end, ticket.getPkDuration(), ticket.getPrice(), addPkDuration,
                    addPrice, ticket.getPrice() + addPrice);
        }
    }

    public record getOrderHistory(
        Long orderId,
        String pkltNm,
        String carNum,
        String time,
        int duration,
        int price
    ) {
        public static getOrderHistory from(Order order, Pklt pklt, Ticket ticket) {
            LocalDateTime start = order.getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a h시 mm분");
            String time = start.format(formatter);
            return new getOrderHistory(order.getOrderId(), pklt.getPkltNm(), order.getCarNum(), time, ticket.getPkDuration(), ticket.getPrice());
        }
    }

}
