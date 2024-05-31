package com.example.lab.dto.ticket;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {

    @Positive(message = "Номер места должен быть положительным числом")
    @NotNull(message = "Номер места не должен быть пустым")
    private Integer seat;

    @Positive(message = "Стоимость должна быть положительным числом")
    @NotNull(message = "Стоимость не должна быть пустой")
    private Double price;

}
