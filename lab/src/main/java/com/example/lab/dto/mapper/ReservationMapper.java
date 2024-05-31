package com.example.lab.dto.mapper;

import com.example.lab.dto.reservation.CreateReservationRequest;
import com.example.lab.dto.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "user.id", source = "request.user")
    @Mapping(target = "ticket.route.id", source = "route")
    @Mapping(target = "ticket.seat", source = "seat")
    Reservation mapToReservation(CreateReservationRequest request, Long route, Integer seat);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "ticket.route.id", source = "route")
    @Mapping(target = "ticket.seat", source = "seat")
    Reservation mapToReservation(Long userId, Long route, Integer seat);

    @Mapping(target = "user", source = "user.login")
    ReservationResponse mapToResponse(Reservation reservation);

}
