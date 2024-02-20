package com.example.lab.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "Логин не должна состоять только из пробельных символов")
    @NotEmpty(message = "Логин не должен быть пустым")
    private String login;

    @NotBlank(message = "Пароль не должен состоять только из пробельных символов")
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

}
