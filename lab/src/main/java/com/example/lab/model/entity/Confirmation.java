package com.example.lab.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "confirmation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Confirmation {

    @Id
    private Long id;

    @Column("request_id")
    private String requestId;

    @Column("reservation_id")
    private Long reservationId;

    @Column
    private Boolean processed;

}
