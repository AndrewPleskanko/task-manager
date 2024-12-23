package org.example.authenticationservice.security;

import static org.example.authenticationservice.utils.UserTestUtils.createUserDto;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.repository.UserRepository;
import org.example.authenticationservice.service.BaseServiceTest;
import org.example.authenticationservice.utils.RoleTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AuthenticationProviderImplTest extends BaseServiceTest {

    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDto user;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        RoleDto createdRole = RoleTestUtils.createRoleDto(1,"ROLE_USER");
        user = createUserDto("test1", "$2a$10$5vvbROzmmXGkfPVaZTyNjODxOEBkobazyMcGWaoSKugSaMLSER0Pq",
                createdRole, "testUser@gmail.com");
    }

    @Test
    @DisplayName("AuthenticationProvider should return authenticated token when password matches")
    @Transactional
    public void testAuthenticate_WhenPasswordMatches_ExpectReturnAuthenticatedToken() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("test1", "123");
        userRepository.save(userMapper.toEntity(user));

        // When
        Authentication result = authenticationProvider.authenticate(authentication);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isAuthenticated());
        Assertions.assertEquals(user.getUsername(), ((User) result.getPrincipal()).getUsername());
        Assertions.assertTrue(passwordEncoder.matches((String) result.getCredentials(), user.getPassword()));
        Assertions.assertEquals(user.getRole().toString(), result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("AuthenticationProvider should throw AuthenticationServiceException when password is wrong")
    @Transactional
    public void testAuthenticate_WhenPasswordIsWrong_ExpectAuthenticationServiceException() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("test1", "1235");
        userRepository.save(userMapper.toEntity(user));

        // Then
        assertThrows(AuthenticationServiceException.class, () -> authenticationProvider.authenticate(authentication));
    }
}