package com.example.lab.service;

import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.model.entity.Role;
import com.example.lab.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Mono<Role> getRoleByName(String name) {
        return roleRepository.findByName(name)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Нет роли с таким названием")));
    }

}
