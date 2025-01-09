package com.team4.ttukttak_parking.domain.tosspayment.service;

import com.team4.ttukttak_parking.domain.tosspayment.dto.TosspaymentRequest;
import com.team4.ttukttak_parking.global.exception.BadRequestException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    /**
     * 결제 전 금액 세션에 저장 메서드
     * @param session
     * @param requestDto
     */
    public void createTempPaymentAmount(HttpSession session,
                                        TosspaymentRequest.TempAmountSession requestDto) {
        session.setAttribute(requestDto.orderNumber(), requestDto.amount());
    }

    /**
     * 결제 인증 후 금액과 결제 전 금액 비교하여 검증, 세션 데이터 삭제 메서드
     * @param session
     * @param requestDto
     */
    public void verifyPaymentAmountAndRemoveSession(HttpSession session, TosspaymentRequest.TempAmountSession requestDto) {
        String amount = (String) session.getAttribute(requestDto.orderNumber());
        verifyPaymentAmount(amount, requestDto.amount());
        // 검증에 사용했던 세션은 삭제
        session.removeAttribute(requestDto.orderNumber());
    }

    private void verifyPaymentAmount(String expectedAmount, String actualAmount) {
        if (!expectedAmount.equals(actualAmount)) {
            throw new BadRequestException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }
}
