package com.example.lab.service;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.model.entity.QTicket;
import com.example.lab.model.entity.Route;
import com.example.lab.model.entity.Ticket;
import com.example.lab.repository.TicketRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RouteService routeService;

    public Ticket createTicket(Ticket ticket) {
        Route route = routeService.getRouteById(ticket.getRoute().getId());
        ticket.setRoute(route);

        return ticketRepository.save(ticket);
    }

    public Page<Ticket> getTickets(TicketFilter filter, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (!Objects.isNull(filter.getDeparture())) {
            predicate.and(QTicket.ticket.route.departure.eq(filter.getDeparture()));
        }

        if (!Objects.isNull(filter.getPrice())) {
            predicate.and(QTicket.ticket.price.loe(filter.getPrice()));
        }

        if (!Objects.isNull(filter.getSource())) {
            predicate.and(QTicket.ticket.route.source.eq(filter.getSource()));
        }

        if (!Objects.isNull(filter.getTarget())) {
            predicate.and(QTicket.ticket.route.destination.eq(filter.getTarget()));
        }

        return ticketRepository.findAll(predicate, pageable);
    }

    public Ticket getTicketByRouteAndSeat(Long route, Integer seat) {
        return ticketRepository.findTicketByRouteIdAndSeat(route, seat)
                .orElseThrow(() -> new EntityNotFoundException("Билета с таким id не существует"));
    }

    public Ticket updateTicket(Ticket updated) {
        Ticket ticket = getTicketByRouteAndSeat(updated.getRoute().getId(), updated.getSeat());
        ticket.setTicket(updated);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long route, Integer seat) {
        if (!ticketRepository.existsByRouteIdAndSeat(route, seat)) {
            throw new EntityNotFoundException("Билета с таким id не существует");
        }

        ticketRepository.deleteByRouteIdAndSeat(route, seat);
    }

}
