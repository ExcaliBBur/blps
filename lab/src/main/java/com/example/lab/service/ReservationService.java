package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.exception.IllegalAccessException;
import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.PrivilegeEnum;
import com.example.lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class)
                .flatMap(auth -> reservationRepository.findReservationByTicket(route, seat)
                        .filter(reservation -> auth.getId().equals(reservation.getUser()) || hasEditPrivilege(auth))
                        .switchIfEmpty(Mono.error(new IllegalAccessException("Недостаточно прав для получения доступа к чужой брони")))
                        .flatMap(Mono::just)
                )
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Бронь на билет с таким id не найдена")));
    }

    private boolean hasEditPrivilege(User auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(PrivilegeEnum.RESERVATION_EDIT_PRIVILEGE.toString()));
    }

}
