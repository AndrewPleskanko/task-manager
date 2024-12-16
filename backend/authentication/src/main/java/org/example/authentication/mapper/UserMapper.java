package org.example.authentication.mapper;

import org.example.authentication.dto.UserDto;
import org.example.authentication.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    UserDto toDto(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "status", target = "status")
    User toEntity(UserDto userDto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password",ignore = true)
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "status", target = "status")
    UserDto toSafeDto(User user);
}