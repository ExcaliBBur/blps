package com.example.lab.dto.mapper;

import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.dto.ticket.TicketResponse;
import com.example.lab.dto.ticket.UpdateTicketRequest;
import com.example.lab.model.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "seat", source = "request.seat")
    Ticket mapToTicket(CreateTicketRequest request, Long route);

    Ticket mapToTicket(UpdateTicketRequest request, Long route, Integer seat);

    TicketResponse mapToResponse(Ticket ticket);

}
