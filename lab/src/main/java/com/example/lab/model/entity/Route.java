package com.example.lab.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "route")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "departure")
    private LocalDate departure;

    @Column(name = "train_number")
    private Long train;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

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
