package com.example.lab.controller;

import com.example.lab.dto.mapper.UserMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.user.CreateUserRequest;
import com.example.lab.dto.user.PageUserResponse;
import com.example.lab.dto.user.UpdateUserRequest;
import com.example.lab.dto.user.UserResponse;
import com.example.lab.model.entity.User;
import com.example.lab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Логин уже занят",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content)
    })

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
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public PageUserResponse getUsers(
            @Valid
            PaginationRequest request
    ) {
        Page<User> users = userService.getUsers(request.formPageRequest());
        List<UserResponse> listUsers = users.getContent()
                .stream().map(userMapper::mapToResponse).toList();

        return PageUserResponse.builder()
                .userResponses(listUsers)
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Пользователя не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content)
    })
    public UserResponse updateUser(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateUserRequest request
    ) {
        User user = userMapper.mapToUser(request, id);

        return userMapper.mapToResponse(userService.updateUser(user));
    }
}
