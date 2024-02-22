package com.example.lab.service;

import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.Ticket;
import com.example.lab.model.entity.User;
import com.example.lab.repository.ReservationRepository;
import com.example.lab.repository.TicketRepository;
import com.example.lab.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Reservation createReservation(Reservation reservation) {
        User user = userRepository.findByLogin(reservation.getUser().getLogin())
                .orElseThrow(() -> new EntityNotFoundException("Человека с таким именем не существует"));
        Ticket ticket = ticketRepository.findById(reservation.getTicket().getId())
                .orElseThrow(() -> new EntityNotFoundException("Билета с таким id не существует"));

        reservation.setUser(user);
        reservation.setTicket(ticket);
        return reservationRepository.save(reservation);
    }

    public Page<Reservation> getReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("Бронь с таким id не найдена");
        }
        reservationRepository.deleteById(id);
    }

    public Reservation updateReservationStatus(Long id, Boolean bought) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронь с таким id не найдена"));
        reservation.setBought(bought);
        return reservationRepository.save(reservation);
    }

}
