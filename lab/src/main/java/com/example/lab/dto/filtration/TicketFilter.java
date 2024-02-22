package com.example.lab.dto.filtration;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TicketFilter {

    @FutureOrPresent(message = "Дата отправления не должна быть в прошлом")
    private LocalDate departure;

    private String source;

    private String target;

    @Positive(message = "Стоимость должна быть положительным числом")
    private Double price;

}
