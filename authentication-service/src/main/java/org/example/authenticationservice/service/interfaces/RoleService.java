package org.example.authenticationservice.service.interfaces;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.entity.Role;

import java.util.List;

public interface RoleService {
    Role createRole(RoleDto roleDto);

    List<Role> getAllRoles();

    Role getRole(Long id);

    Role updateRole(Long id, RoleDto roleDto);

    void deleteRole(Long id);

    Role getRoleByName(String roleName);

    void deleteAllRoles();
}
