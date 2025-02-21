package org.example.authenticationservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.service.UserServiceImpl;
import org.example.authenticationservice.service.interfaces.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        userDto.setEmail("testUser@example.com");

        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("testUser@example.com");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAuthenticate_userAuthentication_success() throws Exception {

        mockMvc.perform(post("/auth/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticate_userAuthentication_failure() throws Exception {
        when(authenticationService.authenticateUser(userDto)).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    public void testAddUser_success() throws Exception {

        mockMvc.perform(post("/auth/v1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("testUser@example.com"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    public void testAddUser_failure_userExists() throws Exception {
        when(userService.createUser(userDto)).thenThrow(new RuntimeException("User already exists"));

        mockMvc.perform(post("/auth/v1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("User already exists"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers_success() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/auth/v1/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser"))
                .andExpect(jsonPath("$[0].email").value("testUser@example.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllUsers_unauthorized() throws Exception {
        mockMvc.perform(get("/auth/v1/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
