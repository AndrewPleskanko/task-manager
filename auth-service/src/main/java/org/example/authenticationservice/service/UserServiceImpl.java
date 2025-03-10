package org.example.authenticationservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.authenticationservice.dto.EmailMessageDto;
import org.example.authenticationservice.dto.UserDto;
import org.example.authenticationservice.entity.Role;
import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.exception.EntityNotFoundException;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.repository.UserRepository;
import org.example.authenticationservice.service.interfaces.EmailEventService;
import org.example.authenticationservice.service.interfaces.RoleService;
import org.example.authenticationservice.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_ENTITY = "User";
    private static final String DEACTIVATION_SUBJECT = "User deactivation";
    private static final String DEACTIVATION_CONTENT = "You have been deactivated on site!";

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final EmailEventService kafkaProducer;
    private final RoleService roleService;

    @Value(value = "${kafka.topic.email.service}")
    private String emailServiceTopicName;

    /**
     * Creates a new user.
     *
     * @param userDto the user data transfer object
     * @return the created user data transfer object
     */
    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        log.info("Starting createUser method with userDto: {}", userDto);

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("User with the same username already exists");
        }

        User user = userMapper.toEntity(userDto);
        String userPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(userPassword);
        user.setPassword(encodedPassword);

        String roleName = userDto.getRole().getName();
        Role role = roleService.getRoleByName(roleName);
        user.setRole(role);
        user.setStatus(true);

        User savedUser = userRepository.save(user);

        log.debug("Saved user: {}", savedUser);

        return savedUser;
    }

    /**
     * Deactivates a user by setting their status to false.
     *
     * @param id the id of the user to deactivate
     * @throws EntityNotFoundException if no user is found with the given id
     */
    @Override
    @Transactional
    public void deactivateUser(Long id) {
        log.info("Starting to deactivate user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, String.valueOf(id)));

        user.setStatus(false);
        userRepository.save(user);
        log.debug("User with ID: {} has been deactivated", id);

        sendDeactivationEmail(user);
    }

    private void sendDeactivationEmail(User user) {

        EmailMessageDto emailMessage = new EmailMessageDto();
        emailMessage.setRecipientEmail(user.getEmail());
        emailMessage.setSubject(DEACTIVATION_SUBJECT);
        emailMessage.setContent(DEACTIVATION_CONTENT);

        try {
            String jsonMessage = objectMapper.writeValueAsString(emailMessage);
            kafkaProducer.sendEmailEvent(emailServiceTopicName, jsonMessage);
            log.info("Deactivation email has been sent to user with ID: {}", user.getId());
        } catch (JsonProcessingException e) {
            log.error("Error converting message to JSON", e);
            throw new RuntimeException("Error converting message to JSON", e);
        }
    }

    /**
     * Retrieves a user by its id.
     *
     * @param id the id of the user to retrieve
     * @return the retrieved user
     * @throws EntityNotFoundException if no user is found with the given id
     */
    @Override
    public User getUser(Long id) {
        log.info("Reading user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, String.valueOf(id)));
    }

    /**
     * Updates an existing user.
     *
     * @param id      the id of the user to update
     * @param userDto the user data transfer object with the new values
     * @return the updated user data transfer object
     */
    @Override
    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        log.info("Starting to update user with ID: {}", id);

        User userToUpdate = userMapper.toEntity(userDto);
        userToUpdate.setId(id);

        String roleName = userDto.getRole().getName();
        Role role = roleService.getRoleByName(roleName);
        userToUpdate.setRole(role);

        userToUpdate.setStatus(userDto.isStatus());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            userToUpdate.setPassword(encodedPassword);
        } else {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, String.valueOf(id)));
            userToUpdate.setPassword(existingUser.getPassword());
        }

        User updatedUser = userRepository.save(userToUpdate);

        log.debug("Updated user: {}", updatedUser);

        return updatedUser;
    }

    /**
     * Deletes a user.
     *
     * @param id the id of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        log.info("Delete user with id: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("User not found with id: {}", id);
            throw new EntityNotFoundException(USER_ENTITY, String.valueOf(id));
        }

        userRepository.deleteById(id);
        log.debug("User deleted by id: {}", id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Getting all users");

        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        log.debug("Found {} users", Optional.of(userDtos.size()));

        return userDtos;
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        return userMapper.toDto(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, String.valueOf(1L)));
    }

    @Override
    public UserDto getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }
}