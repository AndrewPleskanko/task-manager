package org.example.authenticationservice.mapper;

import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}

