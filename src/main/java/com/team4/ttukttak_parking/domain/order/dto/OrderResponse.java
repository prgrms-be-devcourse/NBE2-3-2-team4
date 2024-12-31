package com.team4.ttukttak_parking.domain.order.dto;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.DateFormat;
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
            String end;
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
        String status,
        int duration,
        int price
    ) {
        public static getOrderHistory from(Order order, Pklt pklt, Ticket ticket) {
            LocalDateTime start = order.getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a h시 mm분");
            String time = start.format(formatter);
            String status = switch (order.getStatus().name()) {
                case "WAITING" -> "주차 대기";
                case "PARKING" -> "주차중";
                case "CANCELED" -> "환불";
                default -> "주차 완료";
            };
            return new getOrderHistory(order.getOrderId(), pklt.getPkltNm(), order.getCarNum(), time, status,ticket.getPkDuration(), ticket.getPrice());
        }
    }


    public record OrderList(
            Long pkltStatusDetailId,
            String carNum,
            int price,
            String pkltNm,
            LocalDateTime startTime,
            LocalDateTime endTime


    ) {
//        public static OrderList from(String carNum, BigDecimal price, DateFormat createdAt,String pkltNm ) {
//            return new OrderList(carNum, price, createdAt ,pkltNm);
//        }
    }

}
