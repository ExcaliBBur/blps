package com.example.lab.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponse {

    private Long id;

    @JsonProperty(value = "route_id")
    private Long route;

    private Integer seat;

    private Double price;

}
