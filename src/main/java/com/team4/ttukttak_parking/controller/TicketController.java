package com.team4.ttukttak_parking.controller;


import com.team4.ttukttak_parking.domain.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
@Slf4j
@Tag(name = "🎫 Ticket", description = "주차권 관련 API")
public class TicketController {

    private final TicketService ticketService;

}
