package com.example.aiintegration.service;

import java.util.List;
import java.util.Optional;

import com.example.aiintegration.dto.UserStoryDto;
import com.example.aiintegration.entity.UserStory;

public interface IUserStoryService {
    UserStory createUserStory(String title, String description, Integer priority, String status);

    Optional<UserStory> getUserStoryById(Long id);

    List<UserStory> getAllUserStories();

    UserStory updateUserStory(Long id, String title, String description, Integer priority, String status);

    void deleteUserStory(Long id);

    List<UserStoryDto> getUserStoriesByProjectId(Long projectId);
}