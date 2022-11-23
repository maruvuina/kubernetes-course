package com.epam.course.kubernetes.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class User {

    @Id
    private Long id;

    private String username;

    private Integer amountOfPosts;
}
