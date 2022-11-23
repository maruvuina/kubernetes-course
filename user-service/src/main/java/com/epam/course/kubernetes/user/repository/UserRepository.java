package com.epam.course.kubernetes.user.repository;

import com.epam.course.kubernetes.user.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<Boolean> existsUserByUsername(String username);
}
