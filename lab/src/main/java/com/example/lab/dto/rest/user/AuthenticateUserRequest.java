package com.example.lab.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateUserRequest {

    @NotBlank(message = "Имя пользователя не должно состоять только из пробельных символов")
    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    private String username;

    @NotBlank(message = "Пароль не должен состоять только из пробельных символов")
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

}
