package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.tosspayment.dto.TosspaymentRequest;
import com.team4.ttukttak_parking.domain.tosspayment.service.TossPaymentService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/toss-payments")
@Slf4j
@Tag(name = "💲TossPayment API", description = "토스 결제 관련 API")
public class TossPaymentController {

    private final TossPaymentService tossPaymentService;

    /**
     * 결제를 요청하기 전에 orderNumber와 amount를 세션에 저장하는 controller (결제 요청과 승인 사이에 데이터 무결성을 확인)
     * @param session HttpSession
     * @param requestDto TosspaymentRequest.TempAmountSession
     * @return null
     */
    @Operation(summary = "결제 전 주문금액 세션에 임시 저장 API", description = "주문금액을 세션에 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "임시 저장 성공")})
    @PostMapping("/amounts/session")
    public ResponseEntity<ApiResponse<Object>> createTempPaymentAmount(HttpSession session,
                                                                  @RequestBody TosspaymentRequest.TempAmountSession requestDto) {
        tossPaymentService.createTempPaymentAmount(session, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessWithNoData());
    }

    /**
     * 세션에 저장해둔 amount와 결제 후 amount 비교하여 검증 controller
     * @param session HttpSession
     * @param requestDto TosspaymentRequest.TempAmountSession
     * @return null
     */
    @Operation(summary = "세션에 저장된 금액과 실제 결제 금액 검증 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검증 성공")})
    @PostMapping("/amounts/verify")
    public ResponseEntity<ApiResponse<Object>> verifyPaymentAmount(HttpSession session,
                                                 @RequestBody TosspaymentRequest.TempAmountSession requestDto) {
        tossPaymentService.verifyPaymentAmountAndRemoveSession(session, requestDto);
        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData());
    }


    @Operation(summary = "토스페이먼츠에 결제 승인 요청 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "승인 성공")})
    @PostMapping("/confirm")
    public ResponseEntity<?> getConfirm() {

        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData());
    }
}