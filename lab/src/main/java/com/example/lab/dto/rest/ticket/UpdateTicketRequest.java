package com.example.lab.dto.rest.ticket;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTicketRequest {

    @Positive(message = "Стоимость должна быть положительным числом")
    private Double price;

}
