package com.team4.ttukttak_parking.domain.tosspayment.dto;

public record TosspaymentRequest() {

    public record TempAmountSession(
            String orderNumber,
            String amount
    ) {

    }

    public record PaymentConfirmation(
            String orderNumber,
            String paymentKey,
            String amount
    ) {

    }
}
