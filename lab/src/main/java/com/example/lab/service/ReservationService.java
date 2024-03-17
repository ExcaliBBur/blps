package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.PrivilegeEnum;
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
    public Mono<Void> deleteReservation(Long route, Integer seat, User auth) {
        return getReservationByRouteAndSeat(route, seat, auth)
                .flatMap(reservationRepository::delete);
    }

    public Mono<Reservation> updateReservationStatus(Long route, Integer seat, Boolean bought, User auth) {
        return getReservationByRouteAndSeat(route, seat, auth)
                .flatMap(r -> {
                    r.setBought(bought);
                    return reservationRepository.save(r);
                });
    }

    public Mono<Reservation> getReservationByRouteAndSeat(Long route, Integer seat, User auth) {
        return reservationRepository.findReservationByTicket(route, seat)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Бронь на билет с таким id не найдена"))
                )
                .filter(r -> auth.getId().equals(r.getUser()) ||
                        auth.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority()
                                        .equals(PrivilegeEnum.RESERVATION_EDIT_PRIVILEGE.toString())))
                .switchIfEmpty(Mono.error(
                        new IllegalAccessException("Недостаточно прав для получения доступа к чужой брони"))
                );
    }

}
