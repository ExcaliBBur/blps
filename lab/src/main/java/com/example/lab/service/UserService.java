package com.example.lab.service;

import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.UserRole;
import com.example.lab.model.enumeration.UserStatus;
import com.example.lab.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUserRole(String login, UserRole role) {
        User user = getUserByLogin(login);
        user.setRole(role);

        return userRepository.save(user);
    }

    public User updateUserStatus(String login, UserStatus status) {
        User user = getUserByLogin(login);
        user.setStatus(status);

        return userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с таким login не существует"));
    }

}
