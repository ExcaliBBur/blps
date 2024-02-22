package com.example.lab.controller;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.dto.mapper.TicketMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.dto.ticket.TicketResponse;
import com.example.lab.model.entity.Ticket;
import com.example.lab.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@Tag(name = "tickets", description = "Контроллер для работы с билетами")
@Validated
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать билет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Такой билет уже существует",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content)
    })
    public TicketResponse createTicket(
            @RequestBody
            @Valid
            CreateTicketRequest request
    ) {
        Ticket ticket = ticketMapper.mapToTicket(request);

        ticket = ticketService.createTicket(ticket);

        return ticketMapper.mapToResponse(ticket);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить билеты")
    public Page<TicketResponse> getTickets(
            @Valid
            TicketFilter filter,
            @Valid
            PaginationRequest request
    ) {
        Page<Ticket> tickets = ticketService.getTickets(filter, request.formPageRequest());

        return tickets.map(ticketMapper::mapToResponse);
    }

}
