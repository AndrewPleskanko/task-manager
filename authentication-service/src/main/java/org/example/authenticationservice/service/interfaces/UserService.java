package org.example.authenticationservice.service.interfaces;

import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;

import java.util.List;


public interface UserService {
    User createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    User getUser(Long id);

    User updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    void deactivateUser(Long id);
}
