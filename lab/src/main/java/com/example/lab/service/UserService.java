package com.example.lab.service;

import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import com.example.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return null;
    }

    public Page<User> getUsers(Pageable pageable) {
        return null;
    }

    public User updateUserRole(Long id, UserRole role) {
        return null;
    }

    public User updateUserStatus(Long id, UserStatus status) {
        return null;
    }

}
