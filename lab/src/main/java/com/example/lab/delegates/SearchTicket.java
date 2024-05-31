package com.example.lab.delegates;

import com.example.lab.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("searchTicket")
@RequiredArgsConstructor
public class SearchTicket implements JavaDelegate {
    private final TicketService ticketService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long route = (Long) delegateExecution.getVariable("route");
        Long seat = (Long) delegateExecution.getVariable("seat");
        ticketService.getTicketByRouteAndSeat(route, Math.toIntExact(seat));
    }
}
