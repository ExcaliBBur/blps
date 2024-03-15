package com.example.lab.controller;

import com.example.lab.dto.mapper.RouteMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.dto.route.PageRouteResponse;
import com.example.lab.dto.route.RouteResponse;
import com.example.lab.dto.route.UpdateRouteRequest;
import com.example.lab.model.entity.Route;
import com.example.lab.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<PageRouteResponse> getRoutes(
            @Valid
            PaginationRequest request
    ) {
        Pageable pageable = request.formPageRequest();

        Mono<List<RouteResponse>> routesMono = routeService.getRoutes(pageable)
                .map(routeMapper::mapToResponse)
                .collectList();

        Mono<Long> totalRoutesMono = routeService.countRoutes();

        Mono<Boolean> hasNextPageMono = routeService.hasNextPage(pageable);

        return Mono.zip(routesMono, totalRoutesMono, hasNextPageMono)
                .map(tuple -> new PageRouteResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()));
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
