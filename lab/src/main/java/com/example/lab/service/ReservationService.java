package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.Reservation;
import com.example.lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketService ticketService;
    private final UserService userService;

    public Mono<Reservation> createReservation(Reservation reservation) {
        return userService.userExistsById(reservation.getUser())
                .flatMap(u -> ticketService.ticketExistsById(reservation.getTicket()))
                .flatMap(t -> reservationRepository.save(reservation));
    }

    public Flux<Reservation> getReservations(Pageable pageable) {
        return reservationRepository.findReservations(pageable.getPageSize(), pageable.getPageNumber());
    }

    @Transactional
    public Mono<Void> deleteReservation(Long route, Integer seat) {
        return getReservationByRouteAndSeat(route, seat)
                .flatMap(reservationRepository::delete);
    }

    public Mono<Reservation> updateReservationStatus(Long route, Integer seat, Boolean bought) {
        return getReservationByRouteAndSeat(route, seat)
                .flatMap(r -> {
                    r.setBought(bought);
                    return reservationRepository.save(r);
                });
    }

    public Mono<Reservation> getReservationByRouteAndSeat(Long route, Integer seat) {
        return reservationRepository.findReservationByTicket(route, seat)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Бронь на билет с таким id не найдена")));
    }

}
