package com.example.lab.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "seat")
    private Integer seat;

    @Column(name = "price")
    private Double price;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Reservation reservation;

    public void setTicket(Ticket ticket) {
        if (Objects.nonNull(ticket.price)) {
            this.price = ticket.price;
        }
    }

}
