package com.example.lab.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageRouteResponse {
    List<RouteResponse> routeResponses;

    @JsonProperty(value = "total_elements")
    long totalElements;

    @JsonProperty(value = "total_pages")
    int totalPages;
}
