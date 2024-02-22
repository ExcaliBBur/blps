package com.example.lab.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route")
    private List<Ticket> tickets;

}
