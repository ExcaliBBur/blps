package com.example.lab.controller;

import com.example.lab.dto.mapper.ReservationMapper;
import com.example.lab.dto.mapper.TicketMapper;
import com.example.lab.dto.reservation.CreateReservationRequest;
import com.example.lab.dto.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import com.example.lab.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routes/{route}/tickets")
@Tag(name = "reservations", description = "Контроллер для работы с бронью")
@Validated
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final TicketMapper ticketMapper;

    @PostMapping("/{seat}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать бронь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Билет уже забронирован",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Билета или пользователя не существует",
                    content = @Content)
    })
    public ReservationResponse createReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            @RequestBody
            @Valid
            CreateReservationRequest request
    ) {
        Reservation reservation = reservationMapper.mapToReservation(request, route, seat);

        reservation = reservationService.createReservation(reservation);

        return reservationMapper.mapToResponse(reservation);
    }

    @Operation(summary = "Изменить статус оплаты брони")
    @ApiResponse(responseCode = "404", description = "Брони не существует",
            content = @Content)
    @PatchMapping("/{seat}/reservation/status")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse updateReservationStatus(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            @RequestBody
            Boolean bought
    ) {
        Reservation reservation = reservationService.updateReservationStatus(route, seat, bought);

        return reservationMapper.mapToResponse(reservation);
    }

    @Operation(summary = "Удалить бронь")
    @ApiResponse(responseCode = "404", description = "Брони не существует",
            content = @Content)
    @DeleteMapping("/{seat}/reservation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        reservationService.deleteReservation(route, seat);
    }

    @Operation(summary = "Получить информацию о брони")
    @ApiResponse(responseCode = "404", description = "Брони не существует",
            content = @Content)
    @GetMapping("/{seat}/reservation")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse getReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return reservationMapper.mapToResponse(reservationService.getReservationByRouteAndSeat(route, seat));
    }

}
