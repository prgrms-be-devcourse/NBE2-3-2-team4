package com.team4.ttukttak_parking.controller;

import com.team4.ttukttak_parking.domain.order.dto.OrderRequest.CreateOrder;
import com.team4.ttukttak_parking.domain.order.dto.OrderResponse;
import com.team4.ttukttak_parking.domain.order.service.OrderService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
@Tag(name = "Order Tickets", description = "주차권 주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    // 주차권 구매 요청
    @Operation(summary = "주차권 주문 요청 API", description = "주차권 구매를 요청합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")})
    @PostMapping("/tickets")
    public ResponseEntity<ApiResponse<OrderResponse.CreateOrder>> createOrder(@RequestBody CreateOrder dto,
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
            .body(ApiResponse.createSuccess(orderService.createOrder(dto, user.getUsername())));
    }
}
