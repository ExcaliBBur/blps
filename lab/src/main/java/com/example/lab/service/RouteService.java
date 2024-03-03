package com.example.lab.service;

import com.example.lab.model.entity.Route;
import com.example.lab.repository.RouteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Page<Route> getRoutes(Pageable pageable) {
        return routeRepository.findAll(pageable);
    }

    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new EntityNotFoundException("Маршрута с таким id не найдено");
        }

        routeRepository.deleteById(id);
    }

    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Маршрута с таким id не существует"));
    }

    public Route updateRoute(Route updated) {
        Route route = getRouteById(updated.getId());
        route.setRoute(updated);

        return routeRepository.save(route);
    }

}
