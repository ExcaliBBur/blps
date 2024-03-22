package com.example.lab.controller;

import com.example.lab.dto.mapper.UserMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.user.*;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.RoleEnum;
import com.example.lab.model.enumeration.StatusEnum;
import com.example.lab.service.DetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final DetailsService detailsService;
    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
    @Operation(summary = "Получить пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<PageUserResponse> getUsers(
            @Valid
            PaginationRequest request
    ) {
        Pageable pageable = request.formPageRequest();

        Mono<List<UserResponse>> routesMono = detailsService.getUsers(pageable)
                .map(userMapper::mapToResponse)
                .collectList();

        Mono<Long> totalRoutesMono = detailsService.countUsers();

        Mono<Boolean> hasNextPageMono = detailsService.hasNextPage(pageable);

        return Mono.zip(routesMono, totalRoutesMono, hasNextPageMono)
                .map(tuple -> new PageUserResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
    @Operation(summary = "Получить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<UserResponse> getUser(
            @PathVariable
            Long id
    ) {
        return detailsService.getUserById(id)
                .map(userMapper::mapToResponse);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER_UPDATE_PRIVILEGE')")
    @Operation(summary = "Изменить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Пользователя не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
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

        return detailsService.updateUserCredentials(user)
                .map(userMapper::mapToResponse);
    }

    @PatchMapping("/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER_PROMOTE_PRIVILEGE')")
    @Operation(summary = "Изменить роль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Пользователя не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<UserResponse> updateUserRole(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateRoleRequest updateRoleRequest
    ) {
        User user = userMapper.mapToUser(id);
        user.setRole(RoleEnum.valueOf(updateRoleRequest.getRole()));
        return detailsService.updateUserRole(user)
                .map(userMapper::mapToResponse);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER_UPDATE_PRIVILEGE')")
    @Operation(summary = "Изменить статус пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Пользователя не существует",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Параметры не прошли валидацию",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет необходимых прав доступа",
                    content = @Content)
    })
    public Mono<UserResponse> updateUserStatus(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateStatusRequest updateStatusRequest
    ) {
        User user = userMapper.mapToUser(id);
        user.setStatus(StatusEnum.valueOf(updateStatusRequest.getStatus()));
        return detailsService.updateUserStatus(user)
                .map(userMapper::mapToResponse);
    }

}
