package com.example.lab.service;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.model.entity.QTicket;
import com.example.lab.model.entity.Route;
import com.example.lab.model.entity.Ticket;
import com.example.lab.repository.RouteRepository;
import com.example.lab.repository.TicketRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;

    public Ticket createTicket(Ticket ticket) {
        Route route = routeRepository.findById(ticket.getRoute().getId())
                .orElseThrow(() -> new EntityNotFoundException("Маршрута с таким id не существует"));
        ticket.setRoute(route);
        return ticketRepository.save(ticket);
    }

    public Page<Ticket> getTickets(TicketFilter filter, Pageable pageable) {
        QTicket qTicket = QTicket.ticket;
        BooleanBuilder predicate = new BooleanBuilder();

        if (!Objects.isNull(filter.getDeparture())) {
            predicate.and(qTicket.route.departure.eq(filter.getDeparture()));
        }

        if (!Objects.isNull(filter.getPrice())) {
            predicate.and(qTicket.price.loe(filter.getPrice()));
        }

        if (!Objects.isNull(filter.getSource())) {
            predicate.and(qTicket.route.source.eq(filter.getSource()));
        }

        if (!Objects.isNull(filter.getTarget())) {
            predicate.and(qTicket.route.destination.eq(filter.getTarget()));
        }

        return ticketRepository.findAll(predicate, pageable);
    }

}
