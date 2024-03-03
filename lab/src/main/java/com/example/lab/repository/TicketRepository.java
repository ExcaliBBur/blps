package com.example.lab.repository;

import com.example.lab.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, QuerydslPredicateExecutor<Ticket> {

    Optional<Ticket> findTicketByRouteIdAndSeat(Long route, Integer seat);

    boolean existsByRouteIdAndSeat(Long route, Integer seat);

    void deleteByRouteIdAndSeat(Long route, Integer seat);

}
