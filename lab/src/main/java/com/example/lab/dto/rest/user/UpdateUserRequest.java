package com.example.lab.dto.rest.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Pattern(regexp = "(.*\\S.*|^$)", message = "Имя пользователя не должно состоять только из пробельных символов")
    private String username;

    @Pattern(regexp = "(.*\\S.*|^$)", message = "Пароль не должен состоять только из пробельных символов")
    private String password;

}
