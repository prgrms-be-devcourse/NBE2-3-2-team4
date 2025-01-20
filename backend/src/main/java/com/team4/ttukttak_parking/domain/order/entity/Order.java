package com.team4.ttukttak_parking.domain.order.entity;

import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.order.entity.enums.PayStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatusDetail;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.enums.ParkingStatus;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String carNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pklt_status_detail_id")
    private PkltStatusDetail statusDetail;

    @Enumerated(EnumType.STRING)
    PayStatus payStatus;

    @Enumerated(EnumType.STRING)
    private ParkingStatus status;

    private String orderNumber;
    private LocalDateTime paymentDate;
    private String paymentKey;
    private Long totalAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Order to(String orderNumber, String carNum, Ticket ticket, Member member) {
        return Order.builder()
                .orderNumber(orderNumber)
                .carNum(carNum)
                .ticket(ticket)
                .member(member)
                .status(ParkingStatus.WAITING)
                .payStatus(PayStatus.WAITING)
                .build();
    }

    public void enterPklt(PkltStatusDetail statusDetail) {
        this.statusDetail = statusDetail;
        this.status = ParkingStatus.PARKING;
    }

    public void updateParkingStatus(ParkingStatus parkingStatus) {
        this.status = parkingStatus;
    }

    public void updatePayStatus(PayStatus status) {
        this.payStatus = status;
    }

    public void updatePaymentInfo(String paymentKey, Long totalAmount, LocalDateTime paymentDate) {
        this.paymentKey = paymentKey;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
    }
}
