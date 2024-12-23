package com.team4.ttukttak_parking.domain.order.service;

import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.domain.order.dto.OrderRequest.CreateOrder;
import com.team4.ttukttak_parking.domain.order.dto.OrderResponse;
import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.order.repository.OrderRepository;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import com.team4.ttukttak_parking.domain.ticket.repository.TicketRepository;
import com.team4.ttukttak_parking.global.exception.BadRequestException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse.CreateOrder createOrder(CreateOrder dto, String email) {
        // 회원 검색
        final Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 해당 주차권 검색
        final Ticket ticket = ticketRepository.findById(dto.ticketId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));

        // 주차권 중복 주문 예외처리
        if (orderRepository.existsByCarNumAndAndStatus(dto.carNumber(), ParkingStatus.WAITING)) {
            throw new BadRequestException(ErrorCode.ALREADY_ORDERED);
        }

        // 이미 주차중인 차량 예외처리
        if (orderRepository.existsByCarNumAndAndStatus(dto.carNumber(), ParkingStatus.PARKING)) {
            throw new BadRequestException(ErrorCode.PKLT_ALREADY_PARKED);
        }

        Pklt pklt = ticket.getPklt();

        // 현재 기준 주차장 잔여자리 존재 예외처리
        PkltStatus pkltStatus = pklt.getPkltStatus();
        if (pkltStatus.getTpkct() - pkltStatus.getNowPrkVhclCnt() <= 0) {
            throw new BadRequestException(ErrorCode.PKLT_FULL);
        }

        int total = ticket.getPrice(); // 현재는 할인 혜택 없음

        // 주차권 주문 생성 (주차 대기 상태로 생성, 입차 시 주차중 상태로 변경)
        orderRepository.save(
            Order.to(dto.carNumber(), ticket, member));

        // 주차 현황 주차 차량수 추가
        pkltStatus.updateNowPrkVhclCnt();

        return OrderResponse.CreateOrder.from(
            ticket.getTicketId(), member.getMemberId(), dto.carNumber());
    }

    @Transactional(readOnly = true)
    public OrderResponse.GetOrder getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Ticket ticket = order.getTicket();
        Pklt pklt = ticket.getPklt();
        PkltStatusDetail statusDetail = Optional.ofNullable(order.getStatusDetail())
            .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_PARKED));

        LocalDateTime endTime = Optional.ofNullable(statusDetail.getEndTime())
            .orElse(LocalDateTime.now());
        long minutes = Duration.between(statusDetail.getStartTime(), endTime).toMinutes();

        int basePkDuration = ticket.getPkDuration() * 60;
        int addPkDuration = (int) (minutes - basePkDuration);

        if (addPkDuration <= 0) {
            return OrderResponse.GetOrder.from(pklt, order, statusDetail, ticket, 0, 0);
        }

        int addPrice = calculateAdditionalPrice(pklt, addPkDuration);
        return OrderResponse.GetOrder.from(pklt, order, statusDetail, ticket, addPkDuration,
            addPrice);
    }

    private int calculateAdditionalPrice(Pklt pklt, int addPkDuration) {
        double price5Minute = (double) 12 * pklt.getPkltInfo().getAddPrkCrg() / 5;
        return (int) ((double) addPkDuration / 5 * price5Minute);
    }


    @Transactional
    public String cancelTicket(String email,Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if(!order.getMember().getEmail().equals(email)){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
        //주차 전에만 취소 가능
        if(order.getStatus()== ParkingStatus.WAITING) {
            //주차권 구매 후 10분 지나기 전
            if (order.getCreatedAt().plusMinutes(10).isAfter(LocalDateTime.now())) {
                //상태 변경 & 주차 현황 수 감소
                order.updateParkingStatus(ParkingStatus.CANCELED);
                order.getTicket().getPklt().getPkltStatus().decreaseNowPrkVhclCnt();
                //환불~
                return "전액 환불되었습니다.";
            }
            //구매 후 10분 지남.
            else{
                //상태 변경 & 주차 현황 수 감소
                order.updateParkingStatus(ParkingStatus.CANCELED);
                order.getTicket().getPklt().getPkltStatus().decreaseNowPrkVhclCnt();
                //환불~
                return "구매 후 10분 이상이 경과되어 이용 금액의 50% 환불되었습니다.";
            }
        }
        //이미 주차 중이라면 출차해야 함. 주차권 취소 불가능.
        throw new BadRequestException(ErrorCode.ORDER_CANCEL_UNAVAILABLE);

    }


}

