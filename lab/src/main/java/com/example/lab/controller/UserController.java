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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<UserResponse> createUser(
            @RequestBody
            @Valid
            CreateUserRequest request
    ) {
        User user = userMapper.mapToUser(request);

        return userService.createUser(user)
                .map(userMapper::mapToResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить пользователей")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public Mono<PageUserResponse> getUsers(
            @Valid
            PaginationRequest request
    ) {
        Pageable pageable = request.formPageRequest();

        Mono<List<UserResponse>> routesMono = userService.getUsers(pageable)
                .map(userMapper::mapToResponse)
                .collectList();

        Mono<Long> totalRoutesMono = userService.countUsers();

        Mono<Boolean> hasNextPageMono = userService.hasNextPage(pageable);

        return Mono.zip(routesMono, totalRoutesMono, hasNextPageMono)
                .map(tuple -> new PageUserResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить пользователя")
    @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
            content = @Content)
    public Mono<UserResponse> getUser(
            @PathVariable
            Long id
    ) {
        return userService.getUserById(id)
                .map(userMapper::mapToResponse);
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
    public Mono<UserResponse> updateUser(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateUserRequest request
    ) {
        User user = userMapper.mapToUser(request, id);

        return userService.updateUser(user)
                .map(userMapper::mapToResponse);
    }
}
