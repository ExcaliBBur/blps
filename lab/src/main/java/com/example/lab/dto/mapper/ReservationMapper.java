package com.example.lab.dto.mapper;

import com.example.lab.dto.reservation.CreateReservationRequest;
import com.example.lab.dto.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation mapToReservation(CreateReservationRequest request);

    ReservationResponse mapToResponse(Reservation reservation);

}
