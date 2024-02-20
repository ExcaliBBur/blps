package com.example.lab.dto.mapper;

import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.dto.route.RouteResponse;
import com.example.lab.model.entity.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    Route mapToRoute(CreateRouteRequest request);

    RouteResponse mapToResponse(Route route);

}
