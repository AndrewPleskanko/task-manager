package org.example.authenticationservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.example.authenticationservice.dto.AuthResponseDto;
import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.exception.InvalidCredentialsException;
import org.example.authenticationservice.exception.UserAlreadyExistsException;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.service.interfaces.AuthenticationService;
import org.example.authenticationservice.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationController authenticationController;

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthenticationProvider authenticationProvider;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController, userController).build();
        userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        userDto.setEmail("testUser@example.com");
        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLE_USER");
        userDto.setRole(roleDto);
    }

    @Test
    public void testAddUser_success() throws Exception {
        User user = new User();
        when(userService.createUser(userDto)).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("testUser@example.com"));
    }

    @Test
    public void testAddUser_failure_userExists() throws Exception {
        when(userService.createUser(userDto)).thenThrow(new UserAlreadyExistsException());

        mockMvc.perform(post("/api/v1/auth/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    public void testGetAllUsers_success() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser"))
                .andExpect(jsonPath("$[0].email").value("testUser@example.com"));
    }

    @Test
    public void testGetAllUsers_unauthorized() throws Exception {
        when(userService.getAllUsers()).thenThrow(new InvalidCredentialsException("Unauthorized"));

        mockMvc.perform(get("/api/v1/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
