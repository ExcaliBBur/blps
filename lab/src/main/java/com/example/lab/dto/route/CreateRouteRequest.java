package com.example.lab.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateRouteRequest {

    @FutureOrPresent(message = "Дата отправления не должна быть в прошлом")
    @NotNull(message = "Дата отправления не должна быть пустой")
    private LocalDateTime departure;

    @NotNull(message = "Номер поезда не должен быть пустым")
    @JsonProperty(value = "train_number")
    private Long train;

    @NotBlank(message = "Точка отправления не должна состоять только из пробельных символов")
    @NotEmpty(message = "Точка отправления не должна быть пустой")
    private String source;

    @NotBlank(message = "Точка назначения не должна состоять только из пробельных символов")
    @NotEmpty(message = "Точка назначения не должна быть пустой")
    private String target;

}
