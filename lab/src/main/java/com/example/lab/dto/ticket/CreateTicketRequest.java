package com.example.lab.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {

    @NotNull(message = "Идентификатор маршрута не должен быть пустым")
    @JsonProperty(value = "route_id")
    private Long route;

    @Positive(message = "Номер места должен быть положительным числом")
    @NotNull(message = "Номер места не должен быть пустым")
    private Integer seat;

    @Positive(message = "Стоимость должна быть положительным числом")
    @NotNull(message = "Стоимость не должна быть пустой")
    private Double price;

}
