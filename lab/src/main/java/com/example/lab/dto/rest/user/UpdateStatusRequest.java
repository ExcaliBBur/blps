package com.example.lab.dto.rest.user;

import com.example.lab.constraint.UserStatusConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {
    @Schema(allowableValues = { "ACTIVE", "INACTIVE" })
    @UserStatusConstraint(message = "Такого статуса не существует")
    private String status;
}
