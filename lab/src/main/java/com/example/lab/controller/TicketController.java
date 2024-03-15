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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<TicketResponse> createTicket(
            @PathVariable
            Long route,
            @RequestBody
            @Valid
            CreateTicketRequest request
    ) {
        Ticket ticket = ticketMapper.mapToTicket(request, route);

        return ticketService.createTicket(ticket)
                .map(ticketMapper::mapToResponse);
    }

    @GetMapping("/tickets")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить билеты")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public Mono<PageTicketResponse> getTickets(
            @Valid
            TicketFilter filter,
            @Valid
            PaginationRequest request
    ) {
        Pageable pageable = request.formPageRequest();

        Mono<List<TicketResponse>> routesMono = ticketService.getTickets(filter, pageable)
                .map(ticketMapper::mapToResponse)
                .collectList();

        Mono<Long> totalRoutesMono = ticketService.countTickets(filter);

        Mono<Boolean> hasNextPageMono = ticketService.hasNextPage(filter, pageable);

        return Mono.zip(routesMono, totalRoutesMono, hasNextPageMono)
                .map(tuple -> new PageTicketResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @GetMapping("/routes/{route}/tickets/{seat}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить билет")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public Mono<TicketResponse> getTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return ticketService.getTicketByRouteAndSeat(route, seat)
                .map(ticketMapper::mapToResponse);
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
    public Mono<TicketResponse> updateTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            @RequestBody
            @Valid
            UpdateTicketRequest request
    ) {
        Ticket ticket = ticketMapper.mapToTicket(request, route, seat);

        return ticketService.updateTicket(ticket)
                .map(ticketMapper::mapToResponse);
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
    public Mono<Void> deleteTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return ticketService.deleteTicket(route, seat);
    }

}
