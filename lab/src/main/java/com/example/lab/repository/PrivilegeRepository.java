package com.example.lab.repository;

import com.example.lab.model.entity.Privilege;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PrivilegeRepository extends ReactiveCrudRepository<Privilege, Long> {

    @Query("with recursive role_hierarchy as ( " +
            "select id, name " +
            "from role " +
            "where id = :id " +
            "union all " +
            "select rr.second_role_id as id, r.name " +
            "from role_hierarchy rh " +
            "join role_role rr on rh.id = rr.first_role_id " +
            "join role r on rr.second_role_id = r.id) " +
            "select p.id, p.name " +
            "from role_hierarchy rh " +
            "join role_privilege rp on rh.id = rp.role_id " +
            "join privilege p on rp.privilege_id = p.id")
    Flux<Privilege> findPrivilegesByRoleId(Long id);

}
