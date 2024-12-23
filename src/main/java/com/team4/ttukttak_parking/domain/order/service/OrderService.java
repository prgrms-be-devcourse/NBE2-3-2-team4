package com.team4.ttukttak_parking.domain.order.service;

import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.domain.order.dto.OrderRequest.CreateOrder;
import com.team4.ttukttak_parking.domain.order.dto.OrderResponse;
import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.order.repository.OrderRepository;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import com.team4.ttukttak_parking.domain.ticket.repository.TicketRepository;
import com.team4.ttukttak_parking.global.exception.BadRequestException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}

