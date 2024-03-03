package com.example.lab.dto.mapper;

import com.example.lab.dto.user.CreateUserRequest;
import com.example.lab.dto.user.UpdateUserRequest;
import com.example.lab.dto.user.UserResponse;
import com.example.lab.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(CreateUserRequest request);

    User mapToUser(UpdateUserRequest request, Long id);

    UserResponse mapToResponse(User user);

}
