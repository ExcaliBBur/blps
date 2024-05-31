package com.example.lab.delegates;

import com.example.lab.dto.mapper.ReservationMapperImpl;
import com.example.lab.model.entity.Reservation;
import com.example.lab.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("reservateTicket")
@RequiredArgsConstructor
public class ReservateTicket implements JavaDelegate {
    private final ReservationService reservationService;
    private final ReservationMapperImpl reservationMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long route = (Long) delegateExecution.getVariable("route");
        Long seat = (Long) delegateExecution.getVariable("seat");
        Long userId = (Long) delegateExecution.getVariable("user_id");
        Reservation reservation = reservationMapper.mapToReservation(userId, route, Math.toIntExact(seat));
        reservationService.createReservation(reservation);
    }
}
