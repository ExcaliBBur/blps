package com.example.lab.dto.rest.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RouteResponse {

    private Long id;

    private LocalDate departure;

    @JsonProperty(value = "train_number")
    private Long train;

    private String source;

    private String destination;

}
