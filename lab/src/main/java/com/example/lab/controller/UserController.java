package com.example.lab.controller;

import com.example.lab.constraint.UserRoleConstraint;
import com.example.lab.constraint.UserStatusConstraint;
import com.example.lab.dto.mapper.UserMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.user.CreateUserRequest;
import com.example.lab.dto.user.UserResponse;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import com.example.lab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "users", description = "Контроллер для работы с пользователями")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя")
    @ApiResponse(responseCode = "409", description = "Логин уже занят",
            content = @Content)
    public UserResponse createUser(
            @RequestBody
            @Valid
            CreateUserRequest request
    ) {
        User user = userMapper.mapToUser(request);

        user = userService.createUser(user);

        return userMapper.mapToResponse(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить пользователей")
    public Page<UserResponse> getUsers(
            @Valid
            PaginationRequest request
    ) {
        Page<User> users = userService.getUsers(request.formPageRequest());

        return users.map(userMapper::mapToResponse);
    }

    @PatchMapping("/{login}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить статус пользователя")
    @ApiResponse(responseCode = "404", description = "Пользователя не существует",
            content = @Content)
    public UserResponse updateUserStatus(
            @PathVariable
            String login,
            @RequestBody
            @UserStatusConstraint(message = "Неподходящий статус")
            String status
    ) {
        User user = userService.updateUserStatus(login, UserStatus.valueOf(status));

        return userMapper.mapToResponse(user);
    }

    @PatchMapping("/{login}/role")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить роль пользователя")
    @ApiResponse(responseCode = "404", description = "Пользователя не существует",
            content = @Content)
    public UserResponse updateUserRole(
            @PathVariable
            String login,
            @RequestBody
            @UserRoleConstraint(message = "Неподходящая роль")
            String role
    ) {
        User user = userService.updateUserRole(login, UserRole.valueOf(role));

        return userMapper.mapToResponse(user);
    }
}
