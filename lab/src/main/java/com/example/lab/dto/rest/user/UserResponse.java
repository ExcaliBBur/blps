package com.example.lab.dto.user;

import com.example.lab.model.enumeration.RoleEnum;
import com.example.lab.model.enumeration.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String username;

    private RoleEnum role;

    private StatusEnum status;

}
