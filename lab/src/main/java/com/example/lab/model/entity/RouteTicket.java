package com.example.lab.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("route_ticket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RouteTicket {

    @Id
    private Long id;

    @Column("route_id")
    private Long route;

    @Column("ticket_id")
    private Long ticket;

}
