package org.example.authenticationservice.mapper;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleDto roleDto);
}
