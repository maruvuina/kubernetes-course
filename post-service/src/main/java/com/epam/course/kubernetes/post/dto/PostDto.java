package com.epam.course.kubernetes.post.dto;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

    private Long id;

    @NotNull
    private Long authorId;

    @NotNull
    private String text;

    private Instant postedAt;

    @NotNull
    private String topic;
}
