package org.example.authenticationservice.service.interfaces;


import org.example.authenticationservice.dto.AuthResponseDto;
import org.example.authenticationservice.dto.UserDto;

public interface AuthenticationService {
    AuthResponseDto authenticateUser(UserDto userDto);

    UserDto getAuthenticatedUser();
}
