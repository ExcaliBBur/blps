package com.example.lab.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReservationRequest {

    @JsonProperty(value = "ticket_id")
    private Long ticket;

    @NotBlank(message = "Логин не должна состоять только из пробельных символов")
    @NotEmpty(message = "Логин не должен быть пустым")
    @JsonProperty(value = "user_login")
    private String user;

}
