package org.example.authenticationservice.utils;

import org.example.authenticationservice.dto.RoleDto;

public class RoleTestUtils {
    public static RoleDto createRoleDto(String role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role);
        return roleDto;
    }
}