package com.epam.course.kubernetes.user.mapper;

import com.epam.course.kubernetes.user.dto.UserDto;
import com.epam.course.kubernetes.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User fromDto(UserDto userDto);
}
