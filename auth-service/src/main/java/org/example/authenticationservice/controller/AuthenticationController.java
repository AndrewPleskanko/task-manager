package org.example.authenticationservice.controller;

import org.example.authenticationservice.dto.AuthResponseDto;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.service.interfaces.AuthenticationService;
import org.example.authenticationservice.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param userDto The user authentication request.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("login")
    @Operation(summary = "Authenticate user and generate JWT token", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated and generated token",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication failed")
    })
    public ResponseEntity<AuthResponseDto> authenticate(@Valid @RequestBody UserDto userDto) {
        AuthResponseDto response = authenticationService.authenticateUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Adds a new user.
     *
     * @param userSignUpRequest The user information for registration.
     * @return ResponseEntity indicating the result of the user addition.
     */
    @PostMapping("add")
    @Operation(summary = "Add a new user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully added user",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error, error adding user")
    })
    public ResponseEntity<UserDto> add(@Valid @RequestBody UserDto userSignUpRequest) {
        userService.createUser(userSignUpRequest);
        log.info("User added: {}", userSignUpRequest.getUsername());

        return ResponseEntity.ok(userSignUpRequest);
    }
}