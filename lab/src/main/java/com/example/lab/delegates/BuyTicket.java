package com.example.lab.delegates;

import com.example.lab.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("buyTicket")
@RequiredArgsConstructor
public class BuyTicket implements JavaDelegate {
    private final ReservationService reservationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long route = (Long) delegateExecution.getVariable("route");
        Long seat = (Long) delegateExecution.getVariable("seat");
        Boolean value = (Boolean) delegateExecution.getVariable("value");

        reservationService.updateReservationStatus(route, seat.intValue(), value);
    }
}
