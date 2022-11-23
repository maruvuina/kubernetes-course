package com.epam.course.kubernetes.post.model;

import java.time.Instant;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("posts")
public class Post {

    @Id
    private Long id;

    private Long authorId;

    private String text;

    private Instant postedAt;

}
