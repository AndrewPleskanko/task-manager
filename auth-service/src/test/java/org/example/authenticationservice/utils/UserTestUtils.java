package org.example.authenticationservice.utils;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.dto.UserDto;

public class UserTestUtils {
    public static UserDto createUserDto(String username, String password, RoleDto role, String email) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setRole(role);
        userDto.setEmail(email);
        return userDto;
    }
}
