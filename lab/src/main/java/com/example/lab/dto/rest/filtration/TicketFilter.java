package com.example.lab.dto.rest.filtration;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^(\\d+|\\d+[.]\\d{0,3})",
            message = "Цена должна быть положительным числом с не более 3 знаками после запятой")
    private String price;

}
