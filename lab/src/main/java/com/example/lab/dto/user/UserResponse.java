package com.example.lab.dto.user;

import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String login;

    private UserRole role;

    private UserStatus status;

}
