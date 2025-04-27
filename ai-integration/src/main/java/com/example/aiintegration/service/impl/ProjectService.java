package com.example.aiintegration.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.aiintegration.client.TaskClient;
import com.example.aiintegration.dto.TaskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.aiintegration.converter.ProjectConverter;
import com.example.aiintegration.entity.UserStory;
import com.example.aiintegration.repository.ProjectRepository;
import com.example.aiintegration.repository.UserStoryRepository;
import com.example.aiintegration.service.IProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final UserStoryRepository userStoryRepository;
    private final ObjectMapper objectMapper;

    @Value("${ai.endpoint.url}")
    private String aiEndpoint;

    @Value("${ai.api.key}")
    private String aiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ProjectConverter projectConverter;
    private final TaskClient taskClient;

    @Override
    public List<Map<String, Object>> getUserStoriesForAI(Long projectId) {
        List<UserStory> userStories = userStoryRepository.findByProjectId(projectId);
        return userStories.stream()
                .map(userStory -> {
                    Map<String, Object> aiFormat = projectConverter.convertToAIFormat(userStory, objectMapper);
                    List<TaskDto> tasks = taskClient.getActiveTasksByUserStoryId(userStory.getId());
                    aiFormat.put("tasks", tasks);
                    return aiFormat;
                })
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> processDataWithAi(Long projectId, String prompt) {
        List<Map<String, Object>> projectData = getUserStoriesForAI(projectId);

        if (projectData == null || projectData.isEmpty()) {
            log.warn("No project data found for projectId: {}", projectId);
            return ResponseEntity.badRequest().body("Project not found or has no user stories.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (aiApiKey != null && !aiApiKey.isEmpty()) {
            headers.set("Authorization", "Bearer " + aiApiKey);
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("projectData", projectData);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            log.info("Sending request to AI endpoint: {}", aiEndpoint);
            ResponseEntity<String> response = restTemplate.postForEntity(aiEndpoint, request, String.class);
            log.info("Received response from AI service: status={}, body={}", response.getStatusCode(), response.getBody());
            return response;
        } catch (Exception e) {
            log.error("Error during AI service call: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to call AI service: " + e.getMessage());
        }
    }
}
