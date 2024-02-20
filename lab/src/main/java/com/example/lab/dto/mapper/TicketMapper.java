package com.example.lab.dto.mapper;

import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.dto.ticket.TicketResponse;
import com.example.lab.model.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "route.id", source = "route")
    Ticket mapToTicket(CreateTicketRequest request);

    @Mapping(target = "route", source = "route.id")
    TicketResponse mapToResponse(Ticket ticket);

}
