package com.example.lab.service;

import com.example.lab.model.entity.Reservation;
import com.example.lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation createReservation(Reservation reservation) {
        return null;
    }

    public Page<Reservation> getReservations(Pageable pageable) {
        return null;
    }

    public void deleteReservation(Long id) {

    }

    public Reservation updateReservationStatus(Long id, Boolean bought) {
        return null;
    }

}
