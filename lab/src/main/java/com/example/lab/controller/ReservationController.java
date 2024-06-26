package com.example.lab.controller;

import com.example.lab.dto.mapper.ReservationMapper;
import com.example.lab.dto.rest.reservation.ReservationResponse;
import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.User;
import com.example.lab.service.ReservationService;
import com.example.lab.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/routes/{route}/tickets")
@Tag(name = "reservations", description = "Контроллер для работы с бронью")
@Validated
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final TicketService ticketService;
    private final ReservationMapper reservationMapper;

    @PostMapping("/{seat}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('RESERVATION_CREATE_PRIVILEGE')")
    @Operation(summary = "Создать бронь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Билет уже забронирован",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Билета или пользователя не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<ReservationResponse> createReservation(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Reservation reservation = reservationMapper.mapToReservation(user.getId());

        return ticketService.getTicketByRouteAndSeat(route, seat)
                .flatMap(t -> {
                    reservation.setTicket(t.getId());
                    return reservationService.createReservation(reservation);
                })
                .map(reservationMapper::mapToResponse);
    }

    @PatchMapping("/{seat}/reservation/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('RESERVATION_STATUS_UPDATE_PRIVILEGE')")
    @Operation(summary = "Произвести оплату брони")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Брони не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<Void> updateReservationStatus(
            @PathVariable
            Long route,
            @PathVariable
            Integer seat
    ) {
        return reservationService.buyReservation(route, seat);
    }

    @DeleteMapping("/{seat}/reservation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('RESERVATION_DELETE_PRIVILEGE')")
    @Operation(summary = "Удалить бронь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Брони не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
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
    @PreAuthorize("hasAuthority('RESERVATION_READ_PRIVILEGE')")
    @Operation(summary = "Получить информацию о брони")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Брони не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
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
