package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.exception.IllegalAccessException;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.PrivilegeEnum;
import com.example.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return getUserByUsername(username)
                .flatMap(user -> roleService.getRoleByName(user.getRole().toString())
                        .flatMap(role -> privilegeService.findPrivilegesByRoleId(role.getId())
                                .collectList()
                                .map(privileges -> {
                                    user.setAuthorities(privileges.stream()
                                            .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                                            .collect(Collectors.toList()));
                                    return (UserDetails) user;
                                })
                        )
                );
    }

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

    public Mono<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Пользователя с таким именем не существует")));
    }

    public Mono<User> updateUser(User updated, User auth) {
        return getUserById(updated.getId())
                .filter(user -> user.getId().equals(auth.getId()) ||
                        auth.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority()
                                        .equals(PrivilegeEnum.USER_EDIT_PRIVILEGE.toString())))
                .switchIfEmpty(Mono.error(
                        new IllegalAccessException("Недостаточно прав для редактирования другого пользователя"))
                )
                .flatMap(u -> {
                    u.setUser(updated);
                    return userRepository.save(u);
                });
    }

    public Mono<Long> countUsers() {
        return userRepository.getUsersCount();
    }

    public Mono<Boolean> hasNextPage(Pageable pageable) {
        return userRepository.hasNextPage(pageable.getPageSize(), pageable.getPageNumber());
    }

}
