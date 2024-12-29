package org.example.authenticationservice.service.interfaces;

import java.util.List;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.entity.Role;

public interface RoleService {
    Role createRole(RoleDto roleDto);

    List<Role> getAllRoles();

    Role getRole(Long id);

    Role updateRole(Long id, RoleDto roleDto);

    void deleteRole(Long id);

    Role getRoleByName(String roleName);

    void deleteAllRoles();
}
