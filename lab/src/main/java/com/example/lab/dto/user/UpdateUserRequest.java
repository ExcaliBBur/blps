package com.example.lab.dto.user;

import com.example.lab.constraint.UserRoleConstraint;
import com.example.lab.constraint.UserStatusConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(allowableValues = { "ADMIN", "MODERATOR", "USER" })
    @UserRoleConstraint(message = "Неподходящая роль")
    private String role;

    @Schema(allowableValues = { "ACTIVE", "INACTIVE" })
    @UserStatusConstraint(message = "Неподходящий статус")
    private String status;

}
