package com.epam.course.kubernetes.post.repository;

import com.epam.course.kubernetes.post.model.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends R2dbcRepository<Post, Long> {

}
