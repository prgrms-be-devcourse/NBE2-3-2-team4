package com.team4.ttukttak_parking.domain.ticket.service;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import com.team4.ttukttak_parking.domain.ticket.repository.TicketRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PkltRepository pkltRepository;
    private final PkltInfoRepository pkltInfoRepository;

    @Transactional
    public Void loadDefaultTickets() {
        List<Pklt> pkltList = pkltRepository.findAll();
        int[] nums = {1, 2, 4, 6, 12};

        for (Pklt pklt : pkltList) {
            PkltInfo pkltInfo = pkltInfoRepository.findByPklt(pklt)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

            // 1시간 2시간 4시간 6시간 12시간
            // bsc_prk_crg => 기본 요금
            // bsc_prk_hr => 분단위
            for (int num : nums) {
                int bscPrkCrg = pkltInfo.getBscPrkCrg();
                int bscPrkHr = pkltInfo.getBscPrkHr();

                if (bscPrkCrg == 0) {
                    bscPrkCrg = 260;
                    bscPrkHr = 5;
                }

                int result = (bscPrkCrg / bscPrkHr ) * 60 * num;
                ticketRepository.save(Ticket.to(pklt, result, num));
            }
        }

        return null;
    }

    //주차권 생성
    @Transactional
    public Void addPkltTicket(){
        List<Pklt> pkltList = pkltRepository.findAll();
        int[]nums={2, 3, 4, 6};
        for (Pklt pklt : pkltList) {

            PkltInfo pkltInfo = pkltInfoRepository.findByPklt(pklt)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));
            /*시작시간과 종료시간이 0인 데이터 검증 로직 예외처리
            if((pkltInfo.getWdOperBgngTm()==0&&pkltInfo.getWdOperEndTm()==0)
                    &&(pkltInfo.getWeOperBgngTm()==0&&pkltInfo.getWeOperEndTm()==0)){

                throw new NotFoundException(ErrorCode.PKLT_NOT_DATA_FOUND);

            }*/

            int bscPrice = pkltInfo.getBscPrkCrg()*24;//기본요금(2시간권 가격)
            int addPrice = pkltInfo.getAddPrkCrg()*12;//추가요금(1시간 단위)

            List<Ticket> tickets = new ArrayList<>();
            tickets.add(Ticket.to(pklt, bscPrice,2));
            tickets.add(Ticket.to(pklt, bscPrice + addPrice, 3));
            tickets.add(Ticket.to(pklt, bscPrice + addPrice * 2, 4));
            tickets.add(Ticket.to(pklt, bscPrice + addPrice * 4, 6));

            boolean isPklt24Hours=pkltInfo.getWdOperBgngTm() == 0 && pkltInfo.getWdOperEndTm() == 2400; //24시간 주차장일때
            if (isPklt24Hours) {
                int price24=bscPrice+addPrice*22;
                tickets.add(Ticket.to(pklt, price24, 24));
            }

            ticketRepository.saveAll(tickets);
        }
        return null;

    }
}
