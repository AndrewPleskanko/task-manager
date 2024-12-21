package org.example.authentication.security;

import static org.example.authentication.utils.UserTestUtils.createUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.authentication.dto.RoleDto;
import org.example.authentication.dto.UserDto;
import org.example.authentication.mapper.UserMapper;
import org.example.authentication.repository.UserRepository;
import org.example.authentication.service.BaseServiceTest;
import org.example.authentication.utils.RoleTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserDetailsServiceImplTest extends BaseServiceTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    RoleDto roleDto = RoleTestUtils.createRoleDto(1, "ROLE_USER");
    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional
    public void testLoadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Given
        UserDto userDto = createUserDto("test1", "test123", roleDto, "testUser@gmail.com");
        userRepository.save(userMapper.toEntity(userDto));

        // When
        UserDetails result = userDetailsService.loadUserByUsername(userDto.getUsername());

        // Then
        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getRole().toString(), result.getAuthorities().iterator().next().toString());
    }

    @Test
    public void testLoadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        // Given
        // No user in the database

        // When
        // Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("user"));
    }
}
