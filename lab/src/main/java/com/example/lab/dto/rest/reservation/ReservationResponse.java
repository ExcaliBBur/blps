package com.example.lab.dto.reservation;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponse {

    private Long id;

    @JsonProperty(value = "user_id")
    private Long user;

    private Boolean bought;

}
