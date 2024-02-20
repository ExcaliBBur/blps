package com.example.lab.dto.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @Min(value = 0, message = "Номер страницы должен быть положительным числом")
    @JsonProperty(value = "page_number")
    private Integer pageNumber;

    @Min(value = 1, message = "Размер страницы должен быть >= 1")
    @Max(value = 20, message = "Размер страницы должен быть <= 20")
    @JsonProperty(value = "page_size")
    private Integer pageSize;

    public PageRequest formPageRequest() {
        return PageRequest.of(
                Optional.ofNullable(pageNumber).orElse(0),
                Optional.ofNullable(pageSize).orElse(20)
        );
    }

}
