package com.example.lab.repository;

import com.example.lab.model.entity.Ticket;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Ticket, Long> {

    Mono<Ticket> findTicketByRouteAndSeat(Long route, Integer seat);

    Mono<Boolean> existsByRouteAndSeat(Long route, Integer seat);

    @Modifying
    Mono<Void> deleteByRouteAndSeat(Long route, Integer seat);

    @Query("select * from ticket t " +
            "join route r on r.id = t.route_id " +
            "where (:departure is null or r.departure = :departure) " +
            "and (:price is null or t.price <= :price) " +
            "and (:source is null or r.source = :source) " +
            "and (:destination is null or r.destination = :destination) " +
            "limit :pageSize offset :pageNumber * :pageSize")
    Flux<Ticket> findTickets(
            LocalDate departure,
            Double price,
            String source,
            String destination,
            Integer pageSize,
            Integer pageNumber
    );

}
