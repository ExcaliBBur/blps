package com.example.lab.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
