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
                    "[{\"userId\": \"1\", \"userName\": \"john@example.com\", \"assignedTasks\": [{\"taskId\": \"1\", \"estimatedStartDate\": \"2025-05-05\", \"estimatedEndDate\": \"2025-05-07\"}, {\"taskId\": \"9\", \"estimatedStartDate\": \"2025-05-08\", \"estimatedEndDate\": \"2025-05-09\"}, {\"taskId\": \"3\", \"estimatedStartDate\": \"2025-05-10\", \"estimatedEndDate\": \"2025-05-12\"}, {\"taskId\": \"12\", \"estimatedStartDate\": \"2025-05-13\", \"estimatedEndDate\": \"2025-05-15\"}, {\"taskId\": \"22\", \"estimatedStartDate\": \"2025-05-16\", \"estimatedEndDate\": \"2025-05-18\"}, {\"taskId\": \"25\", \"estimatedStartDate\": \"2025-05-19\", \"estimatedEndDate\": \"2025-05-21\"}]}, {\"userId\": \"2\", \"userName\": \"jane@example.com\", \"assignedTasks\": [{\"taskId\": \"11\", \"estimatedStartDate\": \"2025-05-05\", \"estimatedEndDate\": \"2025-05-06\"}, {\"taskId\": \"15\", \"estimatedStartDate\": \"2025-05-07\", \"estimatedEndDate\": \"2025-05-08\"}, {\"taskId\": \"29\", \"estimatedStartDate\": \"2025-05-09\", \"estimatedEndDate\": \"2025-05-10\"}, {\"taskId\": \"30\", \"estimatedStartDate\": \"2025-05-11\", \"estimatedEndDate\": \"2025-05-12\"}, {\"taskId\": \"32\", \"estimatedStartDate\": \"2025-05-13\", \"estimatedEndDate\": \"2025-05-14\"}]}, {\"userId\": \"3\", \"userName\": \"admin@example.com\", \"assignedTasks\": [{\"taskId\": \"7\", \"estimatedStartDate\": \"2025-05-05\", \"estimatedEndDate\": \"2025-05-06\"}, {\"taskId\": \"14\", \"estimatedStartDate\": \"2025-05-07\", \"estimatedEndDate\": \"2025-05-08\"}, {\"taskId\": \"23\", \"estimatedStartDate\": \"2025-05-09\", \"estimatedEndDate\": \"2025-05-12\"}]}]",
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

        Map<String, TaskDto> allTasks = projectData.stream()
                .flatMap(story -> ((List<TaskDto>) story.get("tasks")).stream())
                .collect(Collectors.toMap(task -> String.valueOf(task.getId()), task -> task, (existing, replacement) -> existing));

        Map<String, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(user -> String.valueOf(user.getId()), user -> user));

        List<Map<String, Object>> result = userAssignments.stream()
                .map(assignment -> {
                    Map<String, Object> userAssignmentResult = new HashMap<>();
                    userAssignmentResult.put("userId", assignment.getUserId());
                    UserDto user = userMap.get(assignment.getUserId());
                    userAssignmentResult.put("userName", user != null ? user.getEmail() : "Unknown User");

                    List<Map<String, Object>> assignedTasks = assignment.getAssignedTasks().stream()
                            .map(assignedTask -> {
                                TaskDto task = allTasks.get(assignedTask.getTaskId());
                                if (task != null) {
                                    Map<String, Object> taskDetails = new HashMap<>();
                                    taskDetails.put("taskId", task.getId());
                                    taskDetails.put("title", task.getTitle());
                                    taskDetails.put("estimatedStartDate", assignedTask.getEstimatedStartDate());
                                    taskDetails.put("estimatedEndDate", assignedTask.getEstimatedEndDate());
                                    taskDetails.put("estimatedHours", task.getEstimatedHours() != null ? task.getEstimatedHours() : task.getEstimatedHours()); // Ensure this is populated
                                    return taskDetails;
                                }
                                return null;
                            })
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
