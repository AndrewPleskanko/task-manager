package org.example.authentication.utils;

import org.example.authentication.dto.RoleDto;

public class RoleTestUtils {
    public static RoleDto createRoleDto(int id, String role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role);
        roleDto.setId((long) id);
        return roleDto;
    }
}
