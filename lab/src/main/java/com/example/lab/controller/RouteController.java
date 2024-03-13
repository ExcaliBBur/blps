package com.example.lab.controller;

import com.example.lab.dto.mapper.RouteMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.dto.route.RouteResponse;
import com.example.lab.dto.route.UpdateRouteRequest;
import com.example.lab.model.entity.Route;
import com.example.lab.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/routes")
@Validated
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;
    private final RouteMapper routeMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RouteResponse> createRoute(
            @RequestBody
            @Valid
            CreateRouteRequest request
    ) {
        Route route = routeMapper.mapToRoute(request);

        return routeService.createRoute(route)
                .map(routeMapper::mapToResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<RouteResponse> getRoutes(
            @Valid
            PaginationRequest request
    ) {
        return routeService.getRoutes(request.formPageRequest())
                .map(routeMapper::mapToResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RouteResponse> getRoutes(
            @PathVariable("id")
            Long id
    ) {
        return routeService.getRouteById(id)
                .map(routeMapper::mapToResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRoute(
            @PathVariable
            Long id
    ) {
        return routeService.deleteRoute(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RouteResponse> updateRoute(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateRouteRequest request
    ) {
        Route route = routeMapper.mapToRoute(request, id);

        return routeService.updateRoute(route)
                .map(routeMapper::mapToResponse);
    }

}
