package org.example.authenticationservice.service.interfaces;

import java.util.List;

import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.dto.UserProjectAuditInfoDto;
import org.example.authenticationservice.entity.User;

public interface UserService {
    User createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    User getUser(Long id);

    User updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    void deactivateUser(Long id);

    UserDto getUserByUsername(String username);

    User findUserByUsername(String username);

    UserDto getAuthenticatedUser();

    List<UserDto> findByProjectId(Long projectId);

    List<UserProjectAuditInfoDto> getUserProjectsWithAudit(Long userId);
}
