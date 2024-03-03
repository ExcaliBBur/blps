package com.example.lab.service;

import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.Ticket;
import com.example.lab.model.entity.User;
import com.example.lab.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketService ticketService;
    private final UserService userService;

    public Reservation createReservation(Reservation reservation) {
        User user = userService.getUserById(reservation.getUser().getId());
        Ticket ticket = ticketService.getTicketByRouteAndSeat(
                reservation.getTicket().getRoute().getId(),
                reservation.getTicket().getSeat()
        );

        reservation.setUser(user);
        reservation.setTicket(ticket);

        return reservationRepository.save(reservation);
    }

    public Page<Reservation> getReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Transactional
    public void deleteReservation(Long route, Integer seat) {
        if (!reservationRepository.existsByTicketRouteIdAndTicketSeat(route, seat)) {
            throw new EntityNotFoundException("Бронь на билет с таким id не найдена");
        }

        reservationRepository.deleteByTicketRouteIdAndTicketSeat(route, seat);
    }

    public Reservation updateReservationStatus(Long route, Integer seat, Boolean bought) {
        Reservation reservation = getReservationByRouteAndSeat(route, seat);
        reservation.setBought(bought);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationByRouteAndSeat(Long route, Integer seat) {
        return reservationRepository.findReservationByTicketRouteIdAndTicketSeat(route, seat)
                .orElseThrow(() -> new EntityNotFoundException("Бронь на билет с таким id не найдена"));
    }

}
