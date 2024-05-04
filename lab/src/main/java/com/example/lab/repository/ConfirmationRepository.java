package com.example.lab.repository;

import com.example.lab.model.entity.Confirmation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ConfirmationRepository extends ReactiveCrudRepository<Confirmation, Long> {

    Mono<Boolean> existsByRequestId(String id);

    Mono<Boolean> existsByReservationId(Long id);

    Mono<Confirmation> findConfirmationByRequestId(String id);

    @Query("update confirmation " +
            "set processed = :processed " +
            "where confirmation.request_id = :id " +
            "returning *")
    Mono<Confirmation> updateConfirmationProcessedByRequestId(String id, Boolean processed);

}
