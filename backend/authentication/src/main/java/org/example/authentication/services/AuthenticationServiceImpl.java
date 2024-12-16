package org.example.authentication.services;

import org.example.authentication.dto.AuthResponseDto;
import org.example.authentication.dto.UserDto;
import org.example.authentication.security.JwtGenerator;
import org.example.authentication.services.interfaces.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for user authentication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final JwtGenerator tokenGenerator;

    /**
     * Authenticates a user based on the provided UserDto.
     *
     * @param userDto The UserDto containing user credentials.
     * @return An AuthResponseDto containing the generated authentication token.
     */
    @Override
    public AuthResponseDto authenticateUser(UserDto userDto) {
        log.info("Received authentication request for user: {}", userDto.getUsername());
        Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUsername(),
                userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenGenerator.generateToken(auth);
        log.info("Generated token for user: {}", userDto.getUsername());
        return new AuthResponseDto(token);
    }
}
