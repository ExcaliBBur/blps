package com.example.lab.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "privilege")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Privilege {

    @Id
    private Long id;

    private String name;

}
