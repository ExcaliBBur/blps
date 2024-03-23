package com.example.lab.controller;

import com.example.lab.dto.mapper.ReservationMapper;
import com.example.lab.dto.mapper.TicketMapper;
import com.example.lab.dto.mapper.UserMapper;
import com.example.lab.dto.reservation.ReservationResponse;
import com.example.lab.dto.ticket.CreateTicketRequest;
import com.example.lab.dto.user.AuthenticateUserRequest;
import com.example.lab.dto.user.UserResponse;
import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.Ticket;
import com.example.lab.model.entity.User;
import com.example.lab.service.AdminPanelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/admin")
@Tag(name = "reservations", description = "Контроллер для работы с админской панелью")
@Validated
@RequiredArgsConstructor
public class AdminPanelController {

    private final AdminPanelService adminPanelService;
    private final TicketMapper ticketMapper;
    private final ReservationMapper reservationMapper;
    private final UserMapper userMapper;

    @PostMapping("/routes/{route}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('TICKET_CREATE_PRIVILEGE') and hasAuthority('RESERVATION_CREATE_PRIVILEGE')")
    @Operation(summary = "Создать билет и бронь на него")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<ReservationResponse> createTicketAndReservation(
            @PathVariable
            Long route,
            @RequestBody
            @Valid
            CreateTicketRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Reservation reservation = reservationMapper.mapToReservation(user.getId());
        Ticket ticket1 = ticketMapper.mapToTicket(request, route);

        return adminPanelService.createTicketAndReservation(ticket1, reservation)
                .map(reservationMapper::mapToResponse);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER_PROMOTE_PRIVILEGE')")
    @Operation(summary = "Создать пользователя и назначить ему роль модератора")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Логин уже существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<UserResponse> createUserAndSetModeratorRole(
            @RequestBody
            @Valid
            AuthenticateUserRequest request
    ) {
        User user = userMapper.mapToUser(request);

        return adminPanelService.createUserAndSetModeratorRole(user)
                .map(userMapper::mapToResponse);
    }
}
