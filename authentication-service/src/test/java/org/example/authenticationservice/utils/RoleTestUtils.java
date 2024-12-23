package org.example.authenticationservice.utils;

import org.example.authenticationservice.dto.RoleDto;

public class RoleTestUtils {
    public static RoleDto createRoleDto(int id, String role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role);
        roleDto.setId((long) id);
        return roleDto;
    }
}
