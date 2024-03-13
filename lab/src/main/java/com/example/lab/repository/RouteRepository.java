package com.example.lab.repository;

import com.example.lab.model.entity.Route;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RouteRepository extends ReactiveCrudRepository<Route, Long> {

    @Query("select * from route " +
            "limit :pageSize offset :pageNumber * :pageSize")
    Flux<Route> findRoutes(Integer pageSize, Integer pageNumber);

}
