package com.example.lab.service;

import com.example.lab.model.entity.Privilege;
import com.example.lab.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public Flux<Privilege> findPrivilegesByRoleId(Long id) {
        return privilegeRepository.findPrivilegesByRoleId(id);
    }

}
