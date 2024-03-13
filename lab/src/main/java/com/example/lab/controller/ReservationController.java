package com.example.lab.controller;

import com.example.lab.dto.mapper.ReservationMapper;
import com.example.lab.dto.reservation.CreateReservationRequest;
import com.example.lab.dto.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import com.example.lab.service.ReservationService;
import com.example.lab.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/routes/{route}/tickets")
@Validated
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final TicketService ticketService;
    private final ReservationMapper reservationMapper;

    @PostMapping("/{seat}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ReservationResponse> createReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            @RequestBody
            @Valid
            CreateReservationRequest request
    ) {
        Reservation reservation = reservationMapper.mapToReservation(request);

        return ticketService.getTicketByRouteAndSeat(route, seat)
                .flatMap(t -> {
                    reservation.setTicket(t.getId());
                    return reservationService.createReservation(reservation);
                })
                .map(reservationMapper::mapToResponse);
    }

    @PatchMapping("/{seat}/reservation/status")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReservationResponse> updateReservationStatus(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            @RequestBody
            Boolean bought
    ) {
        return reservationService.updateReservationStatus(route, seat, bought)
                .map(reservationMapper::mapToResponse);
    }

    @DeleteMapping("/{seat}/reservation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return reservationService.deleteReservation(route, seat);
    }

    @GetMapping("/{seat}/reservation")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReservationResponse> getReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return reservationService.getReservationByRouteAndSeat(route, seat)
                .map(reservationMapper::mapToResponse);
    }

}
