package com.epam.course.kubernetes.post.mapper;

import com.epam.course.kubernetes.post.dto.PostDto;
import com.epam.course.kubernetes.post.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto toDto(Post post);

    Post fromDto(PostDto postDto);
}
