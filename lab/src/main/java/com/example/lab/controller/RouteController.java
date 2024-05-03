package com.example.lab.controller;

import com.example.lab.dto.mapper.RouteMapper;
import com.example.lab.dto.rest.pagination.PaginationRequest;
import com.example.lab.dto.rest.route.CreateRouteRequest;
import com.example.lab.dto.rest.route.PageRouteResponse;
import com.example.lab.dto.rest.route.RouteResponse;
import com.example.lab.dto.rest.route.UpdateRouteRequest;
import com.example.lab.model.entity.Route;
import com.example.lab.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/routes")
@Tag(name = "routes", description = "Контроллер для работы с маршрутами")
@Validated
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;
    private final RouteMapper routeMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROUTE_CREATE_PRIVILEGE')")
    @Operation(summary = "Создать маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
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
    @PreAuthorize("hasAuthority('ROUTE_READ_PRIVILEGE')")
    @Operation(summary = "Получить маршруты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
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
    @PreAuthorize("hasAuthority('ROUTE_READ_PRIVILEGE')")
    @Operation(summary = "Получить маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<RouteResponse> getRoutes(
            @PathVariable("id")
            Long id
    ) {
        return routeService.getRouteById(id)
                .map(routeMapper::mapToResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROUTE_DELETE_PRIVILEGE')")
    @Operation(summary = "Удалить маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<Void> deleteRoute(
            @PathVariable
            Long id
    ) {
        return routeService.deleteRoute(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROUTE_UPDATE_PRIVILEGE')")
    @Operation(summary = "Изменить маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Маршрут успешно изменён",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
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
