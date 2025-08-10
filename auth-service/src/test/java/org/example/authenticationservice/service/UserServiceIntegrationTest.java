package org.example.authenticationservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.example.authenticationservice.dto.RoleDto;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.Role;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.exception.EntityNotFoundException;
import org.example.authenticationservice.mapper.RoleMapper;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.repository.RoleRepository;
import org.example.authenticationservice.repository.UserRepository;
import org.example.authenticationservice.service.interfaces.UserService;
import org.example.authenticationservice.utils.RoleTestUtils;
import org.example.authenticationservice.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTest extends BaseServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    RoleDto roleUserDto;

    @BeforeEach
    void setUp() {
        //Given
        userRepository.deleteAll();
        roleRepository.deleteAll();

        RoleDto roleUserDto = RoleTestUtils.createRoleDto("ROLE_USER");
        RoleDto roleAdminDto = RoleTestUtils.createRoleDto("ROLE_ADMIN");

        Role roleUser = roleMapper.toEntity(roleUserDto);
        Role roleAdmin = roleMapper.toEntity(roleAdminDto);

        roleUser = roleRepository.save(roleUser);
        roleAdmin = roleRepository.save(roleAdmin);
        // Save users in the database
        userRepository.save(new User("test1", "test123", "presF@gmail.com", roleUser, "1234567890", 25, true));
        userRepository.save(new User("test2", "test234", "test2@gmail.com", roleAdmin, "1234567890", 25, true));
        UserDto userDto1 = UserTestUtils.createUserDto("john", "123", roleMapper.toDto(roleUser), "testUser@gmail.com");
        UserDto userDto2 = UserTestUtils.createUserDto("admin", "admin", roleAdminDto, "testAdmin@gmail.com");
        userService.createUser(userDto1);
        userService.createUser(userDto2);

        this.roleUserDto = roleUserDto;
    }

    @Test
    public void createUser_createsNewUser_returnsCreatedUser() {
        // Given
        UserDto userDto = new UserDto(null, "testCreateUser",
                "testCreatePassword", "testCreateUser@gmail.com", roleUserDto, "1234567890", 25, true);

        // When
        User createdUserDto = userService.createUser(userDto);

        // Then
        assertNotNull(createdUserDto.getId());
        assertEquals(userDto.getUsername(), createdUserDto.getUsername());
        assertEquals(userDto.getEmail(), createdUserDto.getEmail());
        assertEquals(userDto.getRole().getName(), createdUserDto.getRole().getName());
    }

    @Test
    public void getAllUsers_createsAndGetsUsers_returnsUsers() {
        // Given
        RoleDto roleDto = new RoleDto("ROLE_USER_GetAllUsers");
        Role save = roleRepository.save(roleMapper.toEntity(roleDto));
        User user1 = new User("testUser1", "testPassword1",
                "testUser1@gmail.com", save, "1234567890", 25, true);
        User user2 = new User("testUser2", "testPassword2",
                "testUser2@gmail.com", save, "1234567890", 25, true);
        userRepository.save(user1);
        userRepository.save(user2);

        // When
        List<UserDto> users = userService.getAllUsers();

        // Then
        assertEquals(6, users.size());
    }

    @Test
    void getUserById_getsUserById_returnsUser() {
        // Given
        User savedUser = userRepository.findAll().getFirst();
        Long id = savedUser.getId();

        // When
        User result = userService.getUser(id);

        // Then
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getRole().getId(), result.getRole().getId());
    }

    @Test
    public void updateUser_updatesUser_returnsUpdatedUser() {
        // Given
        RoleDto roleDto = new RoleDto("ROLE_USER_Update");
        roleRepository.save(roleMapper.toEntity(roleDto));
        UserDto userDto = new UserDto(null, "testUpdateUser",
                "testUpdatePassword", "testUpdateUser@gmail.com", roleDto, "1234567890", 25, true);
        User createdUser = userService.createUser(userDto);

        UserDto updatedUserDto = new UserDto(createdUser.getId(), "updatedUsername",
                "updatedPassword", createdUser.getEmail(), roleDto, createdUser.getPhone(), createdUser.getAge(), true);

        // When
        User updatedUser = userService.updateUser(createdUser.getId(), updatedUserDto);

        // Then
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals(updatedUserDto.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void deleteUser_deletesUser_userDoesNotExist() {
        // Given
        RoleDto roleDto = new RoleDto("ROLE_Delete");
        Role save = roleRepository.save(roleMapper.toEntity(roleDto));
        User user = new User("testUser", "testPassword", "testUser@gmail.com", save, "1234567890", 25, true);
        UserDto userDto = new UserDto(null, user.getUsername(), user.getPassword(),
                user.getEmail(), roleDto, user.getPhone(), user.getAge(), true);
        User createdUserDto = userService.createUser(userDto);

        // When
        userService.deleteUser(createdUserDto.getId());

        // Then
        assertFalse(userRepository.existsById(createdUserDto.getId()));
    }

    @Test
    public void createUser_withNullInput_throwsRuntimeException() {
        // When & Then
        assertThrows(RuntimeException.class, () -> userService.createUser(null));
    }

    @Test
    public void getUser_withInvalidId_throwsUserNotFoundException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(-1L));
    }

    @Test
    public void updateUser_withInvalidId_throwsRuntimeException() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setUsername("Test User");

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(-1L, userDto));
    }

    @Test
    public void deleteUser_withInvalidId_throwsUserNotFoundException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(-1L));
    }
}