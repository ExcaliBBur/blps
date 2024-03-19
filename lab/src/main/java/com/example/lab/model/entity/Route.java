package com.example.lab.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Objects;

@Table("route")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Route {

    @Id
    private Long id;

    private LocalDate departure;

    @Column("train_number")
    private Long train;

    private String source;

    private String destination;

    public void setRoute(Route route) {
        if (Objects.nonNull(route.departure)) {
            this.departure = route.departure;
        }

        if (Objects.nonNull(route.train)) {
            this.train = route.train;
        }

        if (Objects.nonNull(route.source)) {
            this.source = route.source;
        }

        if (Objects.nonNull(route.destination)) {
            this.destination = route.destination;
        }
    }

}
