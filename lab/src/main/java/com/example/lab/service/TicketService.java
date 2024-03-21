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

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Mono<Ticket> createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Flux<Ticket> getTickets(TicketFilter filter, Pageable pageable) {
        Double price = null;
        if (Objects.nonNull(filter.getPrice())) {
            price = Double.parseDouble(filter.getPrice());
        }

        return ticketRepository.findTickets(
                filter.getDeparture(),
                price,
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
        Double price = null;
        if (Objects.nonNull(ticketFilter.getPrice())) {
            price = Double.parseDouble(ticketFilter.getPrice());
        }

        return ticketRepository.getTicketsCount(
                ticketFilter.getDeparture(),
                price,
                ticketFilter.getSource(),
                ticketFilter.getTarget()
        );
    }

    public Mono<Boolean> hasNextPage(TicketFilter ticketFilter, Pageable pageable) {
        Double price = null;
        if (Objects.nonNull(ticketFilter.getPrice())) {
            price = Double.parseDouble(ticketFilter.getPrice());
        }

        return ticketRepository.hasNextPage(
                ticketFilter.getDeparture(),
                price,
                ticketFilter.getSource(),
                ticketFilter.getTarget(),
                pageable.getPageSize(),
                pageable.getPageNumber());
    }

}
