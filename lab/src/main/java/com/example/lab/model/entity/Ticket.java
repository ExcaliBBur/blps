package com.example.lab.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("ticket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ticket {

    @Id
    private Long id;

    @Column("route_id")
    private Long route;

    private Integer seat;

    private Double price;

    public void setTicket(Ticket ticket) {
        if (Objects.nonNull(ticket.price)) {
            this.price = ticket.price;
        }
    }

}
