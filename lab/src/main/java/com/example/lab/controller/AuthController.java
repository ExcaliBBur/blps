package com.example.lab.controller;

import com.example.lab.dto.jwt.ResponseJwt;
import com.example.lab.dto.mapper.UserMapper;
import com.example.lab.dto.user.AuthenticateUserRequest;
import com.example.lab.model.entity.User;
import com.example.lab.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Зарегистрировать пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Логин уже занят",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
    })
    public Mono<ResponseJwt> createUser(
            @RequestBody
            @Valid
            AuthenticateUserRequest request
    ) {
        User user = userMapper.mapToUser(request);

        return authService.register(user);
    }

    @PostMapping("/authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Аутентифицировать пользователя")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public Mono<ResponseJwt> authenticateUser(
            @RequestBody
            @Valid
            AuthenticateUserRequest request
    ) {
        User user = userMapper.mapToUser(request);

        return authService.authenticate(user);
    }

}
