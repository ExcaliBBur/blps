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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminPanelService {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;

    public Mono<Reservation> createTicketAndReservation(Ticket ticket, Reservation reservation) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        return ticketRepository.save(ticket)
                .flatMap(t -> {
                    reservation.setTicket(t.getId());
                    return reservationRepository.save(reservation)
                            .flatMap(r -> {
                                if (Objects.isNull(r)) {
                                    transactionManager.rollback(transaction);
                                } else {
                                    transactionManager.commit(transaction);
                                }
                                return Mono.just(r);
                            });
                });
    }

    public Mono<User> createUserAndSetModeratorRole(User user) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        return userRepository.save(user)
                .flatMap(u -> userRepository.updateUserRole(u.getId(), String.valueOf(RoleEnum.ROLE_MODERATOR)))
                .flatMap(u -> {
                    if (Objects.isNull(u)) {
                        transactionManager.rollback(transaction);
                    } else {
                        transactionManager.commit(transaction);
                    }
                    return Mono.just(u);
                });
    }
}
