package com.epam.course.kubernetes.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotNull
    @NotBlank
    private String username;

    private String amountOfPosts;
}
