package com.example.lab.controller;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.dto.mapper.TicketMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.dto.ticket.TicketResponse;
import com.example.lab.dto.ticket.UpdateTicketRequest;
import com.example.lab.model.entity.Ticket;
import com.example.lab.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping("/routes/{route}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
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
    public Flux<TicketResponse> getTickets(
            @Valid
            TicketFilter filter,
            @Valid
            PaginationRequest request
    ) {
        return ticketService.getTickets(filter, request.formPageRequest())
                .map(ticketMapper::mapToResponse);
    }

    @GetMapping("/routes/{route}/tickets/{seat}")
    @ResponseStatus(HttpStatus.OK)
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
    public Mono<Void> deleteTicket(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return ticketService.deleteTicket(route, seat);
    }

}
