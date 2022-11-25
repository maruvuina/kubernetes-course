package com.epam.course.kubernetes.post.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdate {

    @NotNull
    private String text;

    @NotNull
    private String topic;
}
