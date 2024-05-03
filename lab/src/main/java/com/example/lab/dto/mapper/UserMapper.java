package com.example.lab.dto.mapper;

import com.example.lab.dto.rest.user.AuthenticateUserRequest;
import com.example.lab.dto.rest.user.UpdateUserRequest;
import com.example.lab.dto.rest.user.UserResponse;
import com.example.lab.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(AuthenticateUserRequest request);

    User mapToUser(Long id);

    User mapToUser(UpdateUserRequest request, Long id);

    UserResponse mapToResponse(User user);

}
