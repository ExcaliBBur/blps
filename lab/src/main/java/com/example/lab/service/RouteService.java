package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.Route;
import com.example.lab.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Mono<Route> createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Flux<Route> getRoutes(Pageable pageable) {
        return routeRepository.findRoutes(pageable.getPageSize(), pageable.getPageNumber());
    }

    public Mono<Void> deleteRoute(Long id) {
        return getRouteById(id)
                .flatMap(routeRepository::delete);
    }

    public Mono<Route> getRouteById(Long id) {
        return routeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Маршрута с таким id не найдено")));
    }

    public Mono<Route> updateRoute(Route updated) {
        return getRouteById(updated.getId())
                .flatMap(r -> {
                    r.setRoute(updated);
                    return routeRepository.save(r);
                });
    }

    public Mono<Boolean> routeExistsById(Long id) {
        return routeRepository.existsById(id)
                .flatMap(exists -> exists ? Mono.just(true) :
                        Mono.error(new EntityNotFoundException("Маршрута с таким id не найдено")));
    }

    public Mono<Long> countRoutes() {
        return routeRepository.getRoutesCount();
    }

    public Mono<Boolean> hasNextPage(Pageable pageable) {
        return routeRepository.hasNextPage(pageable.getPageSize(), pageable.getPageNumber());
    }

}
