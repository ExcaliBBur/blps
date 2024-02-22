package com.example.lab.model.entity;

import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.valueOf("USER");

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.valueOf("ACTIVE");

}
