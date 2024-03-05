package com.example.lab.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PageTicketResponse {

    @JsonProperty(value = "tickets")
    List<TicketResponse> ticketResponses;

    @JsonProperty(value = "total_elements")
    long totalElements;

    @JsonProperty(value = "total_pages")
    int totalPages;
}
