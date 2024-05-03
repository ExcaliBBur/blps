package com.example.lab.dto.rest.user;

import com.example.lab.constraint.UserRoleConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    @Schema(allowableValues = { "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER" })
    @UserRoleConstraint(message = "Такой роли не существует")
    private String role;
}
