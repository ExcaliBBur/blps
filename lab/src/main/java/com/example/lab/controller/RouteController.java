package com.example.lab.controller;

import com.example.lab.dto.mapper.RouteMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.dto.route.PageRouteResponse;
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
    @Operation(summary = "Создать маршрут")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
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
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public PageRouteResponse getRoutes(
            @Valid
            PaginationRequest request
    ) {
        Page<Route> routes = routeService.getRoutes(request.formPageRequest());
        List<RouteResponse> listRoutes = routes.getContent()
                .stream().map(routeMapper::mapToResponse).toList();

        return PageRouteResponse.builder()
                .routeResponses(listRoutes)
                .totalElements(routes.getTotalElements())
                .totalPages(routes.getTotalPages())
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить маршрут")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public RouteResponse getRoutes(
            @PathVariable("id")
            Long id
    ) {
        return routeMapper.mapToResponse(routeService.getRouteById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Маршрута не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content)
    })
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
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
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
