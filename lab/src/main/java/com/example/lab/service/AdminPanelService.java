package com.example.lab.service;

import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.Ticket;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.RoleEnum;
import com.example.lab.repository.ReservationRepository;
import com.example.lab.repository.TicketRepository;
import com.example.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminPanelService {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Mono<Reservation> createTicketAndReservation(Ticket ticket, Reservation reservation) {

        return ticketRepository.save(ticket)
                .flatMap(t -> {
                    reservation.setTicket(t.getId());
                    return reservationRepository.save(reservation);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Mono<User> createUserAndSetModeratorRole(User user) {

        return userRepository.save(user)
                .flatMap(u -> userRepository.updateUserRole(u.getId(), String.valueOf(RoleEnum.ROLE_MODERATOR)));
    }
}
