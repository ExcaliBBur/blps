package com.example.lab.repository;

import com.example.lab.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findReservationByTicketRouteIdAndTicketSeat(Long route, Integer seat);

    boolean existsByTicketRouteIdAndTicketSeat(Long route, Integer seat);

    void deleteByTicketRouteIdAndTicketSeat(Long route, Integer seat);

}
