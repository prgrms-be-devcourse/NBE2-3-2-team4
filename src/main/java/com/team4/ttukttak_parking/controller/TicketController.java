package com.team4.ttukttak_parking.controller;


import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.ticket.dto.TicketResponseDto;
import com.team4.ttukttak_parking.domain.ticket.entity.Ticket;
import com.team4.ttukttak_parking.domain.ticket.service.TicketService;
import com.team4.ttukttak_parking.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
@Slf4j
@Tag(name = "🎫 Ticket", description = "주차권 관련 API")
public class TicketController {

    private final TicketService ticketService;



    @Operation(summary = "주차장 별 주차권 리스트 조회", description = "주차장별 주차권 리스트 조회를 할 수 있습니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/pklt/{pkltId}")
    public ResponseEntity<ApiResponse<List<TicketResponseDto.Read>>> getPkltTicket(
            @PathVariable Long pkltId) {
        return ResponseEntity.ok().body(
                ApiResponse.createSuccess(ticketService.getPkltTickets(pkltId)));
    }

}
