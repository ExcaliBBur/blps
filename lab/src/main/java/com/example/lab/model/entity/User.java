package com.example.lab.model.entity;

import com.example.lab.model.enumeration.RoleEnum;
import com.example.lab.model.enumeration.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Objects;

@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    private Long id;

    private String username;

    private String password;

    private RoleEnum role = RoleEnum.ROLE_USER;

    private StatusEnum status = StatusEnum.ACTIVE;

    @Transient
    private List<? extends GrantedAuthority> authorities;

    public void setUser(User user) {
        if (Objects.nonNull(user.username)) {
            this.username = user.username;
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(StatusEnum.ACTIVE);
    }
}
