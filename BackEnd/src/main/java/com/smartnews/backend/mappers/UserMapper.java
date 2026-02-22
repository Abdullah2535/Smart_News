package com.smartnews.backend.mappers;

import com.smartnews.backend.dtos.RegisterUserRequest;
import com.smartnews.backend.dtos.UserDto;
import com.smartnews.backend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserMapper {
    UserDto toUserDto(User user);

    User toEntity(RegisterUserRequest request);
}
