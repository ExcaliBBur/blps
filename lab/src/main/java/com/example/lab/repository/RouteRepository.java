package com.example.lab.repository;

import com.example.lab.model.entity.Route;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RouteRepository extends ReactiveCrudRepository<Route, Long> {

    @Query("select * from route r " +
            "order by r.id " +
            "limit :pageSize offset :pageNumber * :pageSize")
    Flux<Route> findRoutes(Integer pageSize, Integer pageNumber);

    @Query("select count(*) from route")
    Mono<Long> getRoutesCount();

    @Query("select exists(select * from route r " +
            "limit :pageSize offset :pageNumber * :pageSize + 1)")
    Mono<Boolean> hasNextPage(Integer pageSize, Integer pageNumber);

}
