package com.example.lab.dto.user;

import com.example.lab.constraint.UserRoleConstraint;
import com.example.lab.constraint.UserStatusConstraint;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Pattern(regexp = "(.*\\S.*|^$)", message = "Логин не должен состоять только из пробельных символов")
    private String login;

    @Pattern(regexp = "(.*\\S.*|^$)", message = "Пароль не должен состоять только из пробельных символов")
    private String password;

    @UserRoleConstraint(message = "Неподходящая роль")
    private String role;

    @UserStatusConstraint(message = "Неподходящий статус")
    private String status;

}
