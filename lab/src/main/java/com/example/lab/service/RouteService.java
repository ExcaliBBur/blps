package com.example.lab.service;

import com.example.lab.model.entity.Route;
import com.example.lab.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route createRoute(Route route) {
        return null;
    }

    public Page<Route> getRoutes(Pageable pageable) {
        return null;
    }

    public void deleteRoute(Long id) {

    }

}
