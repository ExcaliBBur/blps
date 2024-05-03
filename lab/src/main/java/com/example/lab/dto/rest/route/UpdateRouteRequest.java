package com.example.lab.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateRouteRequest {

    @FutureOrPresent(message = "Дата отправления не должна быть в прошлом")
    private LocalDate departure;

    @JsonProperty(value = "train_number")
    private Long train;

    @Pattern(regexp = "(.*\\S.*|^$)", message = "Точка отправления не должна состоять только из пробельных символов")
    private String source;

    @Pattern(regexp = "(.*\\S.*|^$)", message = "Точка назначения не должна состоять только из пробельных символов")
    private String destination;

}
