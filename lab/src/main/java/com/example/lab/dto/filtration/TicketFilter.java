package com.example.lab.dto.filtration;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketFilter {

    @FutureOrPresent(message = "Дата отправления не должна быть в прошлом")
    private LocalDateTime departure;

    @NotBlank(message = "Точка отправления не должна состоять только из пробельных символов")
    private String source;

    @NotBlank(message = "Точка назначения не должна состоять только из пробельных символов")
    private String target;

    @Positive(message = "Стоимость должна быть положительным числом")
    private Double price;

}
