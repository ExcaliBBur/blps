package com.example.lab.repository;

import com.example.lab.model.entity.Reservation;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReservationRepository extends ReactiveCrudRepository<Reservation, Long> {

    @Query("select * from reservation " +
            "join ticket t on t.id = reservation.ticket_id " +
            "where route_id = :route and seat = :seat")
    Mono<Reservation> findReservationByTicket(Long route, Integer seat);

    @Query("select exists(select * from reservation " +
            "join ticket t on t.id = reservation.ticket_id " +
            "where route_id = :route and seat = :seat)")
    Mono<Boolean> existsByTicket(Long route, Integer seat);

    @Modifying
    @Query("delete from reservation " +
            "where reservation.id in (select reservation.id from reservation " +
            "join ticket t on t.id = reservation.ticket_id " +
            "where route_id = :route and seat = :seat)")
    Mono<Void> deleteByTicket(Long route, Integer seat);

    @Query("call delete_expired_reservation(:time_delay_seconds)")
    Mono<Void> deleteExpired(Integer time_delay_seconds);
}
