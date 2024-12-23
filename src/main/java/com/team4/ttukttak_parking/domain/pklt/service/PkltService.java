package com.team4.ttukttak_parking.domain.pklt.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.order.repository.OrderRepository;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.ticket.dto.TicketResponse;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import com.team4.ttukttak_parking.domain.ticket.repository.TicketRepository;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PkltService {

    private final PkltRepository pkltRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public PkltResponse.GetPklt getPklt(Long pkltId) {
        com.team4.ttukttak_parking.domain.pklt.entity.Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltResponse.GetPklt.from(pklt);
    }

    @Transactional(readOnly = true)
    public PkltResponse.GetPkltInfo getPkltInfo(Long pkltId) {
        com.team4.ttukttak_parking.domain.pklt.entity.Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        return PkltResponse.GetPkltInfo.from(pklt.getPkltInfo());
    }

    @Transactional(readOnly = true)
    public List<PkltResponse.GetNearbyPklt> getNearbyPklt(BigDecimal lat, BigDecimal lng) {
        double KM = 0.3;
        double lngDifference = KM / 111 / (Math.cos(lat.doubleValue()));

        Predicate<Pklt> latFilter = pklt ->
            pklt.getLat().doubleValue() > lat.doubleValue() - (KM / 111)
                && lat.doubleValue() + (KM / 111) > pklt.getLat().doubleValue();
        Predicate<Pklt> lngFilter = pklt ->
            pklt.getLot().doubleValue() > lng.doubleValue() - lngDifference
                && pklt.getLot().doubleValue() < lng.doubleValue() + lngDifference;

        // 필터링된 주차장 리스트
        List<Pklt> nearbyPkltList = pkltRepository.findAll().stream()
            .filter(latFilter.and(lngFilter)).toList();

        return calculateCongestionAndSort(nearbyPkltList);
    }

    @Transactional(readOnly = true)
    public PkltResponse.GetPkltStatus getPkltStatus(Long pkltId) {
        Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        PkltStatus pkltStatus = pklt.getPkltStatus();

        int availableSpots = pkltStatus.getTpkct() - pkltStatus.getNowPrkVhclCnt();

        return PkltResponse.GetPkltStatus.from(pkltStatus, availableSpots);
    }

    private List<PkltResponse.GetNearbyPklt> calculateCongestionAndSort(List<Pklt> pkltList) {
        // 혼잡도 기준으로 정렬 및 전체 항목 반환
        return pkltList.stream().sorted(
                Comparator.comparingDouble(
                    pklt -> {
                        PkltStatus status = pklt.getPkltStatus();
                        return (double) status.getNowPrkVhclCnt() / status.getTpkct() * 100;
                    }))
            .map(pklt -> {
                PkltStatus status = pklt.getPkltStatus();
                double percentage = (double) status.getNowPrkVhclCnt() / status.getTpkct() * 100;
                String congestionStatus;
                if (percentage <= 30) {
                    congestionStatus = "여유";
                } else if (percentage <= 70) {
                    congestionStatus = "보통";
                } else {
                    congestionStatus = "혼잡";
                }
                return PkltResponse.GetNearbyPklt.from(pklt, congestionStatus);
            }).toList();
    }

    @Transactional
    public Void loadDefaultDataByJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(new ClassPathResource("static/data.json").getFile());
        JsonNode dataArray = root.get("DATA");

        for (JsonNode data : dataArray) {
            String pkltName = data.get("pklt_nm").asText();

            Pklt pklt = pkltRepository.findByPkltNm(pkltName);

            if (pklt == null) {
                pkltRepository.save(Pklt.to(data, PkltInfo.to(data), PkltStatus.to(data)));
            } else {
                pklt.getPkltStatus().fixStreetPklt();
            }
        }

        return null;
    }

    @Transactional
    public Void loadDefaultTickets() {
        List<Pklt> pkltList = pkltRepository.findAll();
        int[] nums = {1, 2, 4, 6, 12};

        for (Pklt pklt : pkltList) {
            PkltInfo pkltInfo = pklt.getPkltInfo();

            // 1시간 2시간 4시간 6시간 12시간
            // bsc_prk_crg => 기본 요금
            // bsc_prk_hr => 분단위
            for (int num : nums) {
                int bscPrkCrg = pkltInfo.getBscPrkCrg();
                int bscPrkHr = pkltInfo.getBscPrkHr();

                if (bscPrkCrg == 0) {
                    bscPrkCrg = 220;
                    bscPrkHr = 5;
                }

                int result = (bscPrkCrg / bscPrkHr) * 60 * num;

                pklt.regTicket(Ticket.to(pklt, result, num));
            }
        }

        return null;
    }

    @Transactional
    public PkltResponse.EnterPklt enterPklt(String carNum, Long pkltId) {
        final Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        final Order order = orderRepository.findByCarNumAndStatus(carNum, ParkingStatus.WAITING)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        PkltStatusDetail statusDetail = PkltStatusDetail.to(pklt);

        order.enterPklt(statusDetail);

        return PkltResponse.EnterPklt.from(carNum, pkltId);
    }

    @Transactional
    public PkltResponse.ExitPklt exitPklt(String carNum, Long pkltId) {
        final Pklt pklt = pkltRepository.findById(pkltId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        final Order order = orderRepository.findByCarNumAndStatus(carNum, ParkingStatus.PARKING)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        final PkltInfo pkltInfo = pklt.getPkltInfo();

        final PkltStatusDetail statusDetail = order.getStatusDetail();

        LocalDateTime currTime = LocalDateTime.now();
        LocalDateTime exitTime = statusDetail.getStartTime()
            .plusHours(order.getTicket().getPkDuration()).plusMinutes(10);

        int lateFee = 0;

        if (currTime.isAfter(exitTime)) {
            int min = pkltInfo.getAddPrkHr();
            int fee = pkltInfo.getAddPrkCrg();
            int diff = (int) (currTime.toEpochSecond(null) - exitTime.toEpochSecond(null)) / 60;
            lateFee = (diff / min) * fee;
        }

        statusDetail.exit(currTime);
        order.setStatus(ParkingStatus.EXITED);
        pklt.getPkltStatus().exitPkltCnt();

        return PkltResponse.ExitPklt.from(pkltId, carNum, ParkingStatus.EXITED,
            statusDetail.getStartTime().toLocalTime().toString(),
            currTime.toLocalTime().toString(), order.getTicket().getPrice(), lateFee);
    }
    @Transactional
    public List<TicketResponse> getPkltTicketList(Long pkltId) {
        // 주차장 ID로 주차장 정보 조회
        Pklt pklt = pkltRepository.findById(pkltId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PKLT_NOT_FOUND));

        // 해당 주차장의 주차권 리스트 조회
        List<Ticket> tickets = ticketRepository.findByPklt(pklt);

        return TicketResponse.from(tickets.get())

    }
}


