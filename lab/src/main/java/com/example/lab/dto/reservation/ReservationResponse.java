package com.example.lab.dto.reservation;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponse {

    private Long id;

    @JsonProperty(value = "ticket_id")
    private Long ticket;

    @JsonProperty(value = "user_login")
    private String user;

    private Boolean bought;

}
