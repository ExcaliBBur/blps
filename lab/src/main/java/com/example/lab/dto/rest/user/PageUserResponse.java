package com.example.lab.dto.rest.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageUserResponse {

    @JsonProperty(value = "users")
    List<UserResponse> userResponses;

    @JsonProperty(value = "total_elements")
    long totalElements;

    @JsonProperty(value = "has_next_page")
    boolean next;

}
