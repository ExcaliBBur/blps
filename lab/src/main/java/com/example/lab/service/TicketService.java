package com.example.lab.service;

import com.example.lab.dto.filtration.TicketFilter;
import com.example.lab.model.entity.Ticket;
import com.example.lab.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Ticket createTicket(Ticket ticket) {
        return null;
    }

    public Page<Ticket> getTickets(TicketFilter filter, Pageable pageable) {
        return null;
    }

}
