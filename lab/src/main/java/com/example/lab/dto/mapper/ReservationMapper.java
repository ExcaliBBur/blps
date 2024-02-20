package com.example.lab.dto.mapper;

import com.example.lab.dto.reservation.CreateReservationRequest;
import com.example.lab.dto.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "ticket.id", source = "ticket")
    @Mapping(target = "user.login", source = "user")
    Reservation mapToReservation(CreateReservationRequest request);

    @Mapping(target = "ticket", source = "ticket.id")
    @Mapping(target = "user", source = "user.login")
    ReservationResponse mapToResponse(Reservation reservation);

}
