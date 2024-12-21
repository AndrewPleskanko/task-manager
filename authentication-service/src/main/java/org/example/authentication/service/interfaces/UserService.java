package org.example.authentication.service.interfaces;

import java.util.List;

import org.example.authentication.dto.UserDto;
import org.example.authentication.entity.User;


public interface UserService {
    User createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    User getUser(Long id);

    User updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    void deactivateUser(Long id);
}
