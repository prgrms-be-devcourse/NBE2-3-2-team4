package com.team4.ttukttak_parking.domain.tosspayment.service;

import com.team4.ttukttak_parking.domain.order.entity.Order;
import com.team4.ttukttak_parking.domain.order.entity.enums.PayStatus;
import com.team4.ttukttak_parking.domain.order.repository.OrderRepository;
import com.team4.ttukttak_parking.domain.order.service.OrderService;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import com.team4.ttukttak_parking.domain.tosspayment.dto.TosspaymentRequest;
import com.team4.ttukttak_parking.domain.tosspayment.repository.TossPaymentRepository;
import com.team4.ttukttak_parking.global.exception.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    @Value("${TOSS_PAYMENT_SECRET_KEY}")
    private String SECRET_KEY;

    private final OrderRepository orderRepository;
    private final PkltStatusRepository pkltStatusRepository;
    private static final String CANCEL_REASON = "결제 성공 처리 중 오류 발생";

    /**
     * 결제 전 금액 세션에 저장 메서드
     *
     * @param session
     * @param requestDto
     */
    public void createTempPaymentAmount(HttpSession session,
                                        TosspaymentRequest.TempAmountSession requestDto) {
        session.setAttribute(requestDto.orderNumber(), requestDto.amount());
    }

    /**
     * 결제 인증 후 금액과 결제 전 금액 비교하여 검증, 세션 데이터 삭제 메서드
     *
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

    /**
     * 결제 인증 후 토스페이먼츠 결제 승인 요청
     *
     * @param requestDto TosspaymentRequest.PaymentConfirmation
     * @return Long
     */
    public Long confirmPayment(TosspaymentRequest.PaymentConfirmation requestDto) {
        Order order = null;
        JSONObject confirmPaymentResponse = null;

        // 미리 생성한 결제대기 상태 주문 조회
        // TODO: orderNumber -> orderId로 변환하기
        order = orderRepository.findByOrderNumber(requestDto.orderNumber())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        //토스에 결제요청
        confirmPaymentResponse = requestPaymentConfirmation(requestDto, order);

        // 결제 성공 처리
        updateOrderPaymentAndStatus(order, confirmPaymentResponse);
        return order.getOrderId();
    }

    // 토스페이먼츠 결제 승인 API 요청
    private JSONObject requestPaymentConfirmation(TosspaymentRequest.PaymentConfirmation requestDto, Order order) {
        try {
            // 헤더 인코딩
            final String authorizationHeader = getAuthorizationHeader();
            // API 요청
            HttpResponse<String> response = requestConnect(requestDto, authorizationHeader);

            int responseCode = response.statusCode();
            log.info(responseCode + " " + response.body());

            JSONObject responseJson = new JSONObject(response.body());

            // 결제 승인 요청 실패
            if (responseCode != 200) {
                log.error("결제 승인 요청 실패");
                if (Objects.nonNull(order)) {
                    // 주문 삭제
                    deleteOrder(order);
                    // 자리 복구
                    updatePkltStatusCnt(order.getTicket().getPklt().getPkltStatus());
                }
                throw new TossPaymentConfirmException(responseCode, (String) responseJson.get("message"));
            }
            log.info(responseJson.get("orderName").toString() + responseJson.get("totalAmount").toString());
            return responseJson;

        } catch (IOException e) {
            log.error("결제 승인 요청 값 오류");
            if (Objects.nonNull(order)) {
                deleteOrder(order);
                updatePkltStatusCnt(order.getTicket().getPklt().getPkltStatus());
            }
            throw new TossPaymentException(ErrorCode.TOSS_PAYMENT_CONFIRM_REQUEST_ERROR);
        } catch (InterruptedException e) {
            log.error("파싱 실패");
            if (Objects.nonNull(order)) {
                deleteOrder(order);
                updatePkltStatusCnt(order.getTicket().getPklt().getPkltStatus());
            }
            throw new TossPaymentException(ErrorCode.INVALID_PAYMENT_RESPONSE_JSON);
        }
    }

    private void handlePaymentCancellation(JSONObject jsonObject) {
        try {
            String paymentKey = (String) jsonObject.get("paymentKey");
            requestPaymentCancel(paymentKey, CANCEL_REASON);
//            JSONObject errorResponse = buildErrorResponseJson(
//                    ErrorCode.INTERNAL_SERVER_ERROR,
//                    "서버 내부 오류로 결제가 취소되었습니다."
//            );
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            throw new BadRequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new TossPaymentException(ErrorCode.TOSS_PAYMENT_CANCEL_REQUEST_ERROR);
        }
    }

    private HttpResponse<String> requestConnect(TosspaymentRequest.PaymentConfirmation requestDto, String authorizationHeader)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", authorizationHeader)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(
                        "{\"paymentKey\":\"" + requestDto.paymentKey()
                                + "\",\"orderId\":\"" + requestDto.orderNumber()
                                + "\",\"amount\":\"" + requestDto.amount()
                                + "\"}"))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void requestPaymentCancel(String paymentKey, String cancelReason)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", getAuthorizationHeader())
                .header("Content-Type", "application/json")
                .method("POST",
                        HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        log.info("결제 취소 paymentKey: {}, response: {}", paymentKey, response.body());
    }

    // 토스페이먼츠와 연동하기 위한 인증 Header 설정
    private String getAuthorizationHeader() {
        // "Basic" + 토스페이먼츠 API 시크릿 키 + ":" -> base64인코딩
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    private void updateOrderPaymentAndStatus(Order order, JSONObject jsonObject) {
        try {
            String paymentKey = jsonObject.getString("paymentKey");
            Long totalAmount = (long) jsonObject.get("totalAmount");
            LocalDateTime paymentDate = parseLocalDateTime((String) jsonObject.get("approvedAt"));

            order.updatePaymentInfo(paymentKey, totalAmount, paymentDate);
            order.updatePayStatus(PayStatus.COMPLETE);
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("결제 성공 후 비즈니스 로직 오류");
            handlePaymentCancellation(jsonObject);
            throw new BadRequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private LocalDateTime parseLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    private void updatePkltStatusCnt(PkltStatus pkltStatus) {
        pkltStatus.decreaseNowPrkVhclCnt();
        pkltStatusRepository.save(pkltStatus);
    }
}
