package com.example.lab.dto.mapper;

import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.dto.route.RouteResponse;
import com.example.lab.dto.route.UpdateRouteRequest;
import com.example.lab.model.entity.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    Route mapToRoute(CreateRouteRequest request);

    Route mapToRoute(UpdateRouteRequest request, Long id);

    RouteResponse mapToResponse(Route route);

}
