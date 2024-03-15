package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.User;
import com.example.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Flux<User> getUsers(Pageable pageable) {
        return userRepository.findUsers(pageable.getPageSize(), pageable.getPageNumber());
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Пользователя с таким id не существует")));
    }

    public Mono<User> updateUser(User updated) {
        return getUserById(updated.getId())
                .flatMap(u -> {
                    u.setUser(updated);
                    return userRepository.save(u);
                });
    }

    public Mono<Boolean> userExistsById(Long id) {
        return userRepository.existsById(id)
                .flatMap(exists -> exists ? Mono.just(true) :
                        Mono.error(new EntityNotFoundException("Пользователя с таким id не существует")));
    }

    public Mono<Long> countUsers() {
        return userRepository.getUsersCount();
    }

    public Mono<Boolean> hasNextPage(Pageable pageable) {
        return userRepository.hasNextPage(pageable.getPageSize(), pageable.getPageNumber());
    }

}
