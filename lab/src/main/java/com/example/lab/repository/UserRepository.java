package com.example.lab.repository;

import com.example.lab.model.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("select * from _user u " +
            "order by u.id " +
            "limit :pageSize offset :pageNumber * :pageSize")
    Flux<User> findUsers(Integer pageSize, Integer pageNumber);

    @Query("select count(*) from _user")
    Mono<Long> getUsersCount();

    @Query("select exists(select * from _user " +
            "limit :pageSize offset :pageNumber * :pageSize + 1)")
    Mono<Boolean> hasNextPage(Integer pageSize, Integer pageNumber);

}
