package com.example.lab.controller;

import com.example.lab.dto.mapper.ReservationMapper;
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
@RequestMapping("/reservations")
@Tag(name = "reservations", description = "Контроллер для работы с бронью")
@Validated
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать бронь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Билет уже забронирован",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Билета или пользователя не существует",
                    content = @Content)
    })
    public ReservationResponse createReservation(
            @RequestBody
            @Valid
            CreateReservationRequest request
    ) {
        Reservation reservation = reservationMapper.mapToReservation(request);

        reservation = reservationService.createReservation(reservation);

        return reservationMapper.mapToResponse(reservation);
    }

    @Operation(summary = "Изменить статус оплаты брони")
    @ApiResponse(responseCode = "404", description = "Брони не существует",
            content = @Content)
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse updateReservationStatus(
            @PathVariable
            Long id,
            @RequestBody
            Boolean bought
    ) {
        Reservation reservation = reservationService.updateReservationStatus(id, bought);

        return reservationMapper.mapToResponse(reservation);
    }

    @Operation(summary = "Удалить бронь")
    @ApiResponse(responseCode = "404", description = "Брони не существует",
            content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @PathVariable
            Long id
    ) {
        reservationService.deleteReservation(id);
    }
}
