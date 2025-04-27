package org.example.authenticationservice.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("me")
    @Operation(summary = "Get authenticated user data", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user data",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        UserDto user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
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


    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get users by project ID", description = "Retrieve all users assigned to a specific project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved users by project ID",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "Project not found or no users assigned")
            })
    public ResponseEntity<List<UserDto>> getUsersByProjectId(@PathVariable Long projectId) {
        List<UserDto> users = userService.findByProjectId(projectId);
        return ResponseEntity.ok(users);
    }
}
