package com.team4.ttukttak_parking.domain.pklt.dto;

import com.team4.ttukttak_parking.domain.member.entity.Car;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;

import java.math.BigDecimal;


public record PkltResponse() {

    public record GetPklt(
            Long pkltId,
            String pkltName,
            String address,
            BigDecimal latitude,
            BigDecimal longitude
    ) {

        public static GetPklt from(com.team4.ttukttak_parking.domain.pklt.entity.Pklt pklt) {
            return new GetPklt(
                    pklt.getPkltId(), pklt.getPkltNm(), pklt.getAddr(), pklt.getLat(), pklt.getLot());
        }
    }

    public record GetNearbyPklt(
            Long pkltId,
            BigDecimal latitude,
            BigDecimal longitude,
            String status
    ) {

        public static GetNearbyPklt from(com.team4.ttukttak_parking.domain.pklt.entity.Pklt pklt,
                                         String status) {
            return new GetNearbyPklt(pklt.getPkltId(), pklt.getLat(), pklt.getLot(), status);
        }
    }

    public record GetPkltStatus(
            Long pkltStatusId,
            int availableSpots,
            int usedSpots,
            int totalSpots
    ) {

        public static GetPkltStatus from(PkltStatus pkltStatus, int availableSpots) {
            return new GetPkltStatus(pkltStatus.getPkltStatusId(), availableSpots,
                    pkltStatus.getNowPrkVhclCnt(), pkltStatus.getTpkct());
        }
    }

    public record GetPkltInfo(
            Long pkltCd,
            String prkTypeNm,      // 주차장 종류명
            int wdOperBgngTm,      // 평일 운영 시작시간(HHMM)
            int wdOperEndTm,      // 평일 운영 종료시간(HHMM)
            int weOperBgngTm,      // 주말 운영 시작시간(HHMM)
            int weOperEndTm,      // 주말 운영 종료시간(HHMM)
            int lhldyOperBgngTm,    // 공휴일 운영 시작시간(HHMM)
            int lhldyOperEndTm,   // 공휴일 운영 종료시간(HHMM)
            int bscPrkCrg,       // 기본 주차 요금
            int bscPrkHr,         // 기본 주차 시간(분 단위)
            int addPrkCrg,       // 추가 단위 요금
            int addPrkHr        // 추가 단위 시간(분 단위)
    ) {

        public static GetPkltInfo from(PkltInfo pkltInfo) {
            return new GetPkltInfo(
                    pkltInfo.getPkltCd(), pkltInfo.getPrkTypeNm(), pkltInfo.getWdOperBgngTm(),
                    pkltInfo.getWdOperEndTm(), pkltInfo.getWeOperBgngTm(), pkltInfo.getWeOperEndTm(),
                    pkltInfo.getLhldyOperBgngTm(), pkltInfo.getLhldyOperEndTm(),
                    pkltInfo.getBscPrkCrg(),
                    pkltInfo.getBscPrkHr(), pkltInfo.getAddPrkCrg(), pkltInfo.getAddPrkHr()
            );
        }
    }

    public record EnterPklt(
            String carNum,
            Long pkltId
    ) {

        public static EnterPklt from(String carNum, Long pkltId) {
            return new EnterPklt(carNum, pkltId);
        }
    }

    public record ExitPklt( // 주차장, 차 넘버, 상태, 입차 시간, 출차 시간, 주차 요금, 추가 요금
                            Long pkltId,
                            String carNum,
                            ParkingStatus status,
                            String enterTime,
                            String exitTime,
                            int parkingFee,
                            int addFee
    ) {
        public static ExitPklt from(Long pkltId, String carNum, ParkingStatus status, String enterTime, String exitTime, int parkingFee, int addFee) {
            return new ExitPklt(pkltId, carNum, status, enterTime, exitTime, parkingFee, addFee);
        }
    }

    public record PKltTicketDetail(
            String pkltName,//주차장 이름
            String address, //주차장 주소
            String pkltTime,//운영 시간
            String ticketType,//주차권 종류
            String carNumber,//차량번호
            int addFee,//추가 요금
            int addHour,//추가 요금 시간 단위
            int totalFee//총금액

    ) {
        public static PKltTicketDetail from(Pklt pklt, PkltInfo pkltInfo, Ticket ticket, Car car) {
            String pkltTime = formatOperatingTime(pkltInfo.getWdOperBgngTm(), pkltInfo.getWdOperEndTm());

            // 주차권 유형 생성
            String ticketType = ticket.getPkDuration() + "시간권";

            return new PKltTicketDetail(
                    pklt.getPkltNm(),     // 주차장 이름
                    pklt.getAddr(),       // 주차장 주소
                    pkltTime,             // 평일 운영 시간
                    ticketType,           // 주차권 유형
                    car.getCarNum(),      // 차량 번호
                    pkltInfo.getAddPrkCrg(), // 추가 요금
                    pkltInfo.getAddPrkHr(),  // 추가 요금 단위 시간
                    ticket.getPrice() + pkltInfo.getAddPrkCrg() // 총 금액
            );

        }

        private static String formatOperatingTime(int startTime, int endTime) {
            return String.format("%02d:%02d~%02d:%02d",
                    startTime / 100, startTime % 100, // 시작시간 HH:mm
                    endTime / 100, endTime % 100     // 종료시간 HH:mm
            );
        }

    }
}