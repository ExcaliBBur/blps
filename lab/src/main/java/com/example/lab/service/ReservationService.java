package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.Reservation;
import com.example.lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Mono<Reservation> createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
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
