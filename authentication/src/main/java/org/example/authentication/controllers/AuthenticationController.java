package org.example.authentication.controllers;

import java.util.List;

import org.example.authentication.dto.AuthResponseDto;
import org.example.authentication.dto.UserDto;
import org.example.authentication.entity.User;
import org.example.authentication.services.UserServiceImpl;
import org.example.authentication.services.interfaces.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserServiceImpl userService;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param userDto The user authentication request.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("authenticate")
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

    /**
     * Get all users.
     *
     * @return ResponseEntity indicating the result of the user addition.
     */
    @GetMapping("all")
    @Operation(summary = "Get a user list", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully get user list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "500", description = "No static resource access-denied")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getList() {
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }
}