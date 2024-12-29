package org.example.authenticationservice.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.mapper.RoleMapper;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.security.JwtAuthEntryPoint;
import org.example.authenticationservice.security.JwtGenerator;
import org.example.authenticationservice.security.UserDetailsServiceImpl;
import org.example.authenticationservice.security.WebSecurityConfig;
import org.example.authenticationservice.service.AuthenticationServiceImpl;
import org.example.authenticationservice.service.UserServiceImpl;
import org.example.authenticationservice.utils.RoleTestUtils;
import org.example.authenticationservice.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@Import({WebSecurityConfig.class, JwtGenerator.class, JwtAuthEntryPoint.class, AuthenticationServiceImpl.class})
public class AuthenticationControllerIntegrationTest {

    private UserDto userDtoUser;
    private UserDto userDtoAdmin;
    private List<UserDto> userListDto;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Autowired
    AuthenticationServiceImpl authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserServiceImpl userService;

    RoleDto roleUser;
    RoleDto roleAdmin;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @BeforeEach
    public void setup() {
        roleUser = RoleTestUtils.createRoleDto(1, "USER");
        roleAdmin = RoleTestUtils.createRoleDto(2, "USER");
        userDtoUser = UserTestUtils.createUserDto("john", "123", roleUser, "testUser@gmail.com");
        userDtoAdmin = UserTestUtils.createUserDto("admin", "admin", roleAdmin, "testAdmin@gmail.com");
        userListDto = new ArrayList<>();
        userListDto.add(UserTestUtils.createUserDto("user3", "password3", roleAdmin, "testUser3@gmail.com"));
    }

    @Test
    @DisplayName("Should return a valid access token for a valid user")
    public void testAuthenticate_returnsValidToken() throws Exception {
        mockLoadUserByUsername();

        MvcResult authenticateResult = mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDtoUser)))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(authenticateResult);
    }

    @Test
    @DisplayName("Should add a new user and return the user details")
    public void testAddNewUser_expectSuccess() throws Exception {
        doNothing().when(userService).createUser(any(UserDto.class));
        mockLoadUserByUsername();

        String token = generateAuthToken(userDtoUser);

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(asJsonString(userDtoUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDtoUser.getUsername()))
                .andExpect(jsonPath("$.password").value(userDtoUser.getPassword()))
                .andExpect(jsonPath("$.role.name").value(userDtoUser.getRole().getName()));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should return a list of all users")
    public void testGetList_returnsAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(userListDto);
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User("admin",
                        "$2a$10$H7Tw60M/fe.vwwBgxCTvYuDrHGOhJ6s.yxArIrjsgfQOwL6lR2RdO",
                        roleMapper.toEntity(roleAdmin)));
        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        userDtoAdmin.getUsername(),
                        userDtoAdmin.getPassword()));
        String token = generateAuthToken(userDtoAdmin);

        mockMvc.perform(get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].username", hasItems("user1", "user2", "user3")))
                .andExpect(jsonPath("$.[*].password", hasItems("password1", "password2", "password3")))
                .andExpect(jsonPath("$.[*].role.name", containsInAnyOrder(roleUser.getName(),
                        roleUser.getName(), roleAdmin.getName())));
    }

    @Test
    @DisplayName("Should return 403 Forbidden for an unauthorized user")
    public void testGetList_returnsForbidden() throws Exception {
        when(userService.getAllUsers()).thenReturn(userListDto);
        mockLoadUserByUsername();

        String token = generateAuthToken(userDtoUser);

        mockMvc.perform(get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for an existing user")
    public void testAddNewUser_returnsServerError() throws Exception {
        doThrow(new DataIntegrityViolationException("User already exists"))
                .when(userService).createUser(any(UserDto.class));
        mockLoadUserByUsername();
        String token = generateAuthToken(userDtoUser);

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(asJsonString(userDtoUser)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("User already exists"));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized for an invalid user")
    public void testAuthenticate_returnsUnauthorized() throws Exception {
        UserDto invalidUserDto = new UserDto();
        invalidUserDto.setUsername("invalid");
        invalidUserDto.setRole(roleUser);

        when(userDetailsService.loadUserByUsername(anyString()))
                .thenThrow(new UsernameNotFoundException("User not found"));
        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidUserDto)))
                .andExpect(status().isUnauthorized());
    }

    private String generateAuthToken(UserDto userDto) throws Exception {
        MvcResult authenticateResult = mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        String response = authenticateResult.getResponse().getContentAsString();
        return extractTokenFromResponse(response);
    }

    private void mockLoadUserByUsername() {
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User("john",
                        "$2a$10$5vvbROzmmXGkfPVaZTyNjODxOEBkobazyMcGWaoSKugSaMLSER0Pq", roleMapper.toEntity(roleUser)));
        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        userDtoUser.getUsername(),
                        userDtoUser.getPassword()));
    }

    private String extractTokenFromResponse(String response) {
        // Extract token from response string (this is a placeholder, implement as needed)
        return response.substring(response.indexOf("token\":\"") + 8, response.indexOf("\"}"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
