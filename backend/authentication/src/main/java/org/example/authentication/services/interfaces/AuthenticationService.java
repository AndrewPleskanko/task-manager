package org.example.authentication.services.interfaces;

import org.example.authentication.dto.AuthResponseDto;
import org.example.authentication.dto.UserDto;

public interface AuthenticationService {
    AuthResponseDto authenticateUser(UserDto userDto);
}
