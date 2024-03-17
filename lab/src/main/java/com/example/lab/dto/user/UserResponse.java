package com.example.lab.dto.user;

import com.example.lab.model.enumeration.UserRoleEnum;
import com.example.lab.model.enumeration.UserStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String username;

    private UserRoleEnum role;

    private UserStatusEnum status;

}
