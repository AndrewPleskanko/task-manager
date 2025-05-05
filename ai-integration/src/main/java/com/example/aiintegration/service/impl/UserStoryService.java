package com.example.aiintegration.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aiintegration.dto.UserStoryDto;
import com.example.aiintegration.entity.UserStory;
import com.example.aiintegration.exception.ResourceNotFoundException;
import com.example.aiintegration.repository.UserStoryRepository;
import com.example.aiintegration.service.IUserStoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserStoryService implements IUserStoryService {
    private final UserStoryRepository userStoryRepository;

    public UserStoryService(UserStoryRepository userStoryRepository) {
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public UserStory createUserStory(String title, String description, Integer priority, String status) {
        log.info("Creating a new UserStory with title: {}", title);
        UserStory userStory = new UserStory();
        userStory.setTitle(title);
        userStory.setDescription(description);
        userStory.setPriority(priority);
        userStory.setStatus(status);
        return userStoryRepository.save(userStory);
    }

    @Override
    public Optional<UserStory> getUserStoryById(Long id) {
        log.info("Fetching UserStory with id: {}", id);
        return userStoryRepository.findById(id);
    }

    @Override
    public List<UserStory> getAllUserStories() {
        log.info("Fetching all UserStories");
        return userStoryRepository.findAll();
    }

    @Override
    public UserStory updateUserStory(Long id, String title, String description, Integer priority, String status) {
        log.info("Updating UserStory with id: {}", id);
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory not found with id: " + id));
        userStory.setTitle(title);
        userStory.setDescription(description);
        userStory.setPriority(priority);
        userStory.setStatus(status);
        return userStoryRepository.save(userStory);
    }

    @Override
    public void deleteUserStory(Long id) {
        log.info("Deleting UserStory with id: {}", id);
        if (!userStoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserStory not found with id: " + id);
        }
        userStoryRepository.deleteById(id);
    }

    @Override
    public List<UserStoryDto> getUserStoriesByProjectId(Long projectId) {
        log.info("Fetching UserStories for projectId: {}", projectId);
        List<UserStory> userStories = userStoryRepository.findByProjectId(projectId);
        return userStories.stream()
                .map(userStory -> new UserStoryDto(
                        userStory.getId(),
                        userStory.getTitle(),
                        userStory.getDescription(),
                        userStory.getStatus(),
                        userStory.getStoryPoints(),
                        userStory.getPriority(),
                        userStory.getTags(),
                        userStory.getAcceptanceCriteria()
                ))
                .collect(Collectors.toList());
    }
}
