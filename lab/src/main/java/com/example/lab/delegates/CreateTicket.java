package com.example.lab.delegates;

import com.example.lab.dto.mapper.TicketMapper;
import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.inject.Named;


@Named("createTicket")
@RequiredArgsConstructor
public class CreateTicket implements JavaDelegate {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long route = (Long) delegateExecution.getVariable("route");
        Long seat = (Long) delegateExecution.getVariable("seat");
        Long price = (Long) delegateExecution.getVariable("price");

        CreateTicketRequest dto = new CreateTicketRequest(seat.intValue(), price.doubleValue());
        ticketService.createTicket(ticketMapper.mapToTicket(dto, route));
    }
}
