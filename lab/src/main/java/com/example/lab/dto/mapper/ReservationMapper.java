package com.example.lab.dto.mapper;

import com.example.lab.dto.rest.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation mapToReservation(Long user);

    ReservationResponse mapToResponse(Reservation reservation);

}
