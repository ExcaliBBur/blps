package com.example.lab.controller;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.dto.mapper.TicketMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.dto.ticket.PageTicketResponse;
import com.example.lab.dto.ticket.TicketResponse;
import com.example.lab.dto.ticket.UpdateTicketRequest;
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

import java.util.List;

@RestController
@Tag(name = "tickets", description = "Контроллер для работы с билетами")
@Validated
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping("/routes/{route}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать билет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content)
    })
    public TicketResponse createTicket(
            @PathVariable
            Long route,
            @RequestBody
            @Valid
            CreateTicketRequest request
    ) {
        Ticket ticket = ticketMapper.mapToTicket(request, route);

        ticket = ticketService.createTicket(ticket);

        return ticketMapper.mapToResponse(ticket);
    }

    @GetMapping("/tickets")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить билеты")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public PageTicketResponse getTickets(
            @Valid
            TicketFilter filter,
            @Valid
            PaginationRequest request
    ) {
        Page<Ticket> tickets = ticketService.getTickets(filter, request.formPageRequest());
        List<TicketResponse> listTickets = tickets.getContent()
                .stream().map(ticketMapper::mapToResponse).toList();

        return PageTicketResponse.builder()
                .ticketResponses(listTickets)
                .totalElements(tickets.getTotalElements())
                .totalPages(tickets.getTotalPages())
                .build();
    }

    @GetMapping("/routes/{route}/tickets/{seat}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить билет")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public TicketResponse getTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return ticketMapper.mapToResponse(ticketService.getTicketByRouteAndSeat(route, seat));
    }

    @PatchMapping("/routes/{route}/tickets/{seat}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить билет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Билета не существует",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Билет успешно изменён",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content)
    })
    public TicketResponse updateTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            @RequestBody
            @Valid
            UpdateTicketRequest request
    ) {
        Ticket ticket = ticketMapper.mapToTicket(request, route, seat);

        return ticketMapper.mapToResponse(ticketService.updateTicket(ticket));
    }

    @DeleteMapping("/routes/{route}/tickets/{seat}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить билет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Билета не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content)
    })
    public void deleteTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        ticketService.deleteTicket(route, seat);
    }

}
