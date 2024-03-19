package com.example.lab.service;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.Ticket;
import com.example.lab.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Mono<Ticket> createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Flux<Ticket> getTickets(TicketFilter filter, Pageable pageable) {
        return ticketRepository.findTickets(
                filter.getDeparture(),
                Double.parseDouble(filter.getPrice()),
                filter.getSource(),
                filter.getTarget(),
                pageable.getPageSize(),
                pageable.getPageNumber()
        );
    }

    public Mono<Ticket> getTicketByRouteAndSeat(Long route, Integer seat) {
        return ticketRepository.findTicketByRouteAndSeat(route, seat)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Билета с таким id не существует")));
    }

    public Mono<Ticket> updateTicket(Ticket updated) {
        return getTicketByRouteAndSeat(updated.getRoute(), updated.getSeat())
                .flatMap(t -> {
                    t.setTicket(updated);
                    return ticketRepository.save(t);
                });
    }

    public Mono<Void> deleteTicket(Long route, Integer seat) {
        return getTicketByRouteAndSeat(route, seat)
                .flatMap(ticketRepository::delete);
    }

    public Mono<Long> countTickets(TicketFilter ticketFilter) {
        return ticketRepository.getTicketsCount(
                ticketFilter.getDeparture(),
                Double.parseDouble(ticketFilter.getPrice()),
                ticketFilter.getSource(),
                ticketFilter.getTarget()
        );
    }

    public Mono<Boolean> hasNextPage(TicketFilter filter, Pageable pageable) {
        return ticketRepository.hasNextPage(filter, pageable.getPageSize(), pageable.getPageNumber());
    }

}
