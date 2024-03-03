package com.example.lab.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReservationRequest {

    @NotNull(message = "Идентификатор пользователя не должен быть пустым")
    @JsonProperty(value = "user_id")
    private Long user;

}
