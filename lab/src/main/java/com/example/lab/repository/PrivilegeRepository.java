package com.example.lab.repository;

import com.example.lab.model.entity.Privilege;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PrivilegeRepository extends ReactiveCrudRepository<Privilege, Long> {

    @Query("select * from privilege p " +
            "join role_privilege rp on p.id = rp.privilege_id " +
            "where rp.role_id = :role")
    Flux<Privilege> findPrivilegesByRoleId(Long id);

}
