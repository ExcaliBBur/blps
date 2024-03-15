package com.example.lab.controller;

import com.example.lab.dto.mapper.UserMapper;
import com.example.lab.dto.pagination.PaginationRequest;
import com.example.lab.dto.user.CreateUserRequest;
import com.example.lab.dto.user.PageUserResponse;
import com.example.lab.dto.user.UpdateUserRequest;
import com.example.lab.dto.user.UserResponse;
import com.example.lab.model.entity.User;
import com.example.lab.service.UserService;
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
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    public Mono<UserResponse> getUser(
            @PathVariable
            Long id
    ) {
        return userService.getUserById(id)
                .map(userMapper::mapToResponse);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
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
