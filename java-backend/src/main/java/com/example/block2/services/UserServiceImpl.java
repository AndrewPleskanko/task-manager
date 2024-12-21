package com.example.block2.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.block2.dto.EmailMessageDto;
import com.example.block2.dto.GroupResponseDto;
import com.example.block2.dto.UserDto;
import com.example.block2.dto.UserFilterDto;
import com.example.block2.dto.UserReportDto;
import com.example.block2.dto.UserUploadResultDto;
import com.example.block2.entity.Role;
import com.example.block2.entity.User;
import com.example.block2.enums.UserReportType;
import com.example.block2.exceptions.EntityNotFoundException;
import com.example.block2.mapper.UserMapper;
import com.example.block2.repositories.UserRepository;
import com.example.block2.services.interfaces.RoleService;
import com.example.block2.services.interfaces.UserService;
import com.example.block2.services.userimport.UserReportStrategy;
import com.example.block2.services.userimport.UserReportStrategyFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
    private final RoleService roleService;
    private final UserReportStrategyFactory reportStrategyFactory;
    private final ObjectMapper objectMapper;
    private final EmailEventProducerImpl kafkaProducer;

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
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, id));

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
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, id));
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
                    .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY, id));
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
            throw new EntityNotFoundException(USER_ENTITY, id);
        }

        userRepository.deleteById(id);
        log.debug("User deleted by id: {}", id);
    }

    /**
     * Lists users with a given filter and pagination.
     *
     * @param filter the user filter data transfer object
     * @return a page of users
     */
    @Override
    public GroupResponseDto<UserDto> listUsers(UserFilterDto filter) {
        log.info("Starting to list users with filter: {}", filter);

        Specification<User> spec = createSpecification(filter);

        int size = Optional.ofNullable(filter.getSize()).orElse(10);
        int page = Optional.ofNullable(filter.getPage()).orElse(0);

        Pageable pageable = PageRequest.of(page, size);

        Page<User> usersPage = userRepository.findAll(spec, pageable);

        List<UserDto> usersDto = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        log.debug("Found {} users from {} to {}", usersDto.size(),
                pageable.getOffset(), pageable.getOffset() + usersDto.size());

        return new GroupResponseDto<>(usersDto, usersPage.getTotalPages());
    }

    /**
     * Generates a report for users matching a given filter.
     *
     * @param filter the user filter data transfer object
     * @return the generated report as a byte array
     */
    @Override
    public UserReportDto generateReport(UserFilterDto filter) {
        log.info("Generating report for filter: {}", filter);
        List<User> users = getAllUsersMatchingFilter(filter);

        UserReportType reportType = filter.getReportType();
        UserReportStrategy reportStrategy = reportStrategyFactory.lookup(reportType);

        byte[] content = reportStrategy.generateReport(users);
        String reportName = reportStrategy.generateReportName();

        log.debug("Report generated with name: {}", reportName);

        return new UserReportDto(content, reportName);
    }

    public List<User> getAllUsersMatchingFilter(UserFilterDto filter) {
        log.info("Starting to get all users matching filter: {}", filter);

        Specification<User> spec = createSpecification(filter);
        List<User> users = userRepository.findAll(spec);

        log.debug("Found {} users matching filter", users.size());

        return users;
    }

    private Specification<User> createSpecification(UserFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRoleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), filter.getRoleId()));
            }
            if (filter.getUsername() != null) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + filter.getUsername() + "%"));
            }
            if (filter.getEmail() != null) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + filter.getEmail() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public UserUploadResultDto uploadUsersFromFile(MultipartFile multipartFile) {
        log.info("Starting to upload users file");

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        try (InputStream inputStream = multipartFile.getInputStream();
             ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            List<UserDto> users = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            List<User> savedUsers = new ArrayList<>();

            users.parallelStream().forEach(user -> {
                Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
                if (violations.isEmpty()) {
                    savedUsers.add(userMapper.toEntity(user));
                    successCount.incrementAndGet();
                } else {
                    failureCount.incrementAndGet();
                    log.warn("Validation failed for user: {}", user);
                }
            });
            userRepository.saveAll(savedUsers);

            log.info("Finished processing file. Success count: {}. Failure count: {}.",
                    successCount.get(), failureCount.get());
        } catch (Exception e) {
            log.error("Error processing upload file", e);
        }

        return new UserUploadResultDto(successCount.get(), failureCount.get());
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Getting all users");

        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        log.debug("Found {} users", userDtos.size());

        return userDtos;
    }
}