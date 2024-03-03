package com.example.lab.controller;

import com.example.lab.dto.mapper.RouteMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.dto.route.RouteResponse;
import com.example.lab.dto.route.UpdateRouteRequest;
import com.example.lab.model.entity.Route;
import com.example.lab.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Создать маршрут")
    public RouteResponse createRoute(
            @RequestBody
            @Valid
            CreateRouteRequest request
    ) {
        Route route = routeMapper.mapToRoute(request);

        route = routeService.createRoute(route);

        return routeMapper.mapToResponse(route);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить маршруты")
    public Page<RouteResponse> getRoutes(
            @Valid
            PaginationRequest request
    ) {
        Page<Route> routes = routeService.getRoutes(request.formPageRequest());

        return routes.map(routeMapper::mapToResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить маршрут")
    @ApiResponse(responseCode = "404", description = "Маршрута не существует",
            content = @Content)
    public void deleteRoute(
            @PathVariable
            Long id
    ) {
        routeService.deleteRoute(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Маршрут успешно изменён",
                    content = @Content)
    })
    public RouteResponse updateRoute(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateRouteRequest request
    ) {
        Route route = routeMapper.mapToRoute(request, id);

        return routeMapper.mapToResponse(routeService.updateRoute(route));
    }

}
