package org.example.authentication.mapper;

import org.example.authentication.dto.RoleDto;
import org.example.authentication.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "name", target = "name")
    RoleDto toDto(Role role);

    @Mapping(source = "name", target = "name")
    Role toEntity(RoleDto roleDto);
}