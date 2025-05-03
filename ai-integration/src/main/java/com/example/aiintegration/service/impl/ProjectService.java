package com.example.aiintegration.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.aiintegration.dto.UserAssignmentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.aiintegration.client.TaskClient;
import com.example.aiintegration.client.UserClient;
import com.example.aiintegration.converter.ProjectConverter;
import com.example.aiintegration.dto.TaskDto;
import com.example.aiintegration.dto.UserDto;
import com.example.aiintegration.entity.UserStory;
import com.example.aiintegration.repository.UserStoryRepository;
import com.example.aiintegration.service.IProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final UserStoryRepository userStoryRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ProjectConverter projectConverter;
    private final TaskClient taskClient;
    private final UserClient userClient;

    @Value("${GEMINI_ENDPOINT_URL}")
    private String aiBaseEndpointUrl;

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;

    @Value("${AI_DEFAULT_PROMPT}")
    private String defaultAiPromptTemplate;

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

    @Override
    public String processDataWithAi(Long projectId) {
        List<Map<String, Object>> projectData = getUserStoriesForAI(projectId);
        if (projectData == null || projectData.isEmpty()) {
            log.warn("No user stories found for projectId: {}", projectId);
            throw new IllegalArgumentException("Project not found or has no user stories.");
        }

        List<UserDto> users = getUsersByProjectId(projectId);
        if (users == null || users.isEmpty()) {
            log.warn("No users found for projectId: {}", projectId);
            throw new IllegalArgumentException("No users found for the project.");
        }

        try {
            String projectDataJson = objectMapper.writeValueAsString(projectData);
            String usersJson = objectMapper.writeValueAsString(users);

            String finalPrompt = defaultAiPromptTemplate
                    .replace("{{projectData}}", projectDataJson)
                    .replace("{{users}}", usersJson);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(Map.of("parts", List.of(Map.of("text", finalPrompt)))));
            log.info("Prompt Final: {}", finalPrompt);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String aiEndpoint = aiBaseEndpointUrl + geminiApiKey;

//            ResponseEntity<String> response = restTemplate.postForEntity(aiEndpoint, request, String.class);
//            log.info("Received response from AI service: status={}, body={}",
//                    response.getStatusCode(), response.getBody());
//            return response.getBody();

            return processAiResponse(
                    "[{\"userId\": \"1\", \"userName\": \"john@example.com\", \"assignedTasks\": [\"1\", \"9\", \"3\", \"12\", \"22\", \"25\"]}, {\"userId\": \"2\", \"userName\": \"jane@example.com\", \"assignedTasks\": [\"11\", \"15\", \"29\", \"30\", \"32\"]}, {\"userId\": \"3\", \"userName\": \"admin@example.com\", \"assignedTasks\": [\"7\", \"14\", \"23\"]}]",
                    projectData,
                    users
            );

        } catch (JsonProcessingException e) {
            log.error("JSON processing error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert data to JSON.", e);
        } catch (Exception e) {
            log.error("Unexpected error during AI prompt processing or service call: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while processing AI data or calling the AI service.", e);
        }
    }

    private String processAiResponse(String aiResponse, List<Map<String, Object>> projectData, List<UserDto> users) throws JsonProcessingException {
        List<UserAssignmentDto> userAssignments = objectMapper.readValue(aiResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, UserAssignmentDto.class));

        // Створюємо мапу для швидкого доступу до завдань за їх ID
        Map<String, TaskDto> allTasks = projectData.stream()
                .flatMap(story -> ((List<TaskDto>) story.get("tasks")).stream())
                .collect(Collectors.toMap(task -> String.valueOf(task.getId()), task -> task));

        // Створюємо мапу для швидкого доступу до користувачів за їх ID
        Map<String, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(user -> String.valueOf(user.getId()), user -> user));

        List<Map<String, Object>> result = userAssignments.stream()
                .map(assignment -> {
                    Map<String, Object> userAssignmentResult = new HashMap<>();
                    userAssignmentResult.put("userId", assignment.getUserId());
                    UserDto user = userMap.get(assignment.getUserId());
                    userAssignmentResult.put("userName", user != null ? user.getEmail() : "Unknown User"); // Або user.getName(), якщо є

                    List<TaskDto> assignedTasks = assignment.getAssignedTasks().stream()
                            .map(allTasks::get)
                            .filter(java.util.Objects::nonNull)
                            .collect(Collectors.toList());
                    userAssignmentResult.put("assignedTasks", assignedTasks);
                    return userAssignmentResult;
                })
                .collect(Collectors.toList());

        return objectMapper.writeValueAsString(result);
    }

    @Override
    public List<UserDto> getUsersByProjectId(Long projectId) {
        return userClient.getUserByProjectId(projectId);
    }
}
