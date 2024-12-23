package com.team4.ttukttak_parking.domain.order.dto;

import com.team4.ttukttak_parking.domain.order.entity.Order;

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
        Order order
    ) {

        public static GetOrder from(Order order) {
            return new GetOrder(order);
        }
    }


    public record OrderList(
            String carNum,
            BigDecimal price,
            DateFormat createdAt,
            String pkltNm
    ) {
//        public static OrderList from(String carNum, BigDecimal price, LocalDateTime createdAt,String pkltNm ) {
//            return new OrderList(carNum, price, createdAt ,pkltNm);
//        }
    }

}
