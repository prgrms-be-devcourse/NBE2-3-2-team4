package com.team4.ttukttak_parking.domain.tosspayment.entity;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TossPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tossPaymentId;

    @Column(nullable = false, unique = true)
    private String tossPaymentKey;

    @Column(nullable = false)
    private String tossOrderId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private long totalAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TossPaymentMethod tossPaymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TossPaymentStatus tossPaymentStatus;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;
}
