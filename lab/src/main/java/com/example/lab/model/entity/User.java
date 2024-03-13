package com.example.lab.model.entity;

import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private Long id;

    private String login;

    private String password;

    private UserRole role = UserRole.USER;

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
