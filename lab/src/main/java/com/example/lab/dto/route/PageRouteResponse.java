package com.example.lab.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRouteResponse {

    @JsonProperty(value = "routes")
    List<RouteResponse> routeResponses;

    @JsonProperty(value = "total_elements")
    long totalElements;

    @JsonProperty(value = "has_next_page")
    boolean next;

}
