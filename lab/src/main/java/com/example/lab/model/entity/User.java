package com.example.lab.model.entity;

import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    public void setUser(User user) {
        if (Objects.nonNull(user.login)) {
            this.login = user.login;
        }

        if (Objects.nonNull(user.password)) {
            this.password = user.password;
        }

        if (Objects.nonNull(user.role)) {
            this.role = user.role;
        }

        if (Objects.nonNull(user.status)) {
            this.status = user.status;
        }
    }

}
