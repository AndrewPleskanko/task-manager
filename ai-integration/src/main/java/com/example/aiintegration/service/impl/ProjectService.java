package com.example.aiintegration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.aiintegration.dto.AssignedTaskDto;
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
                    "[\n" +
                            "  {\n" +
                            "    \"userId\": \"1\",\n" +
                            "    \"userName\": \"john@example.com\",\n" +
                            "    \"assignedTasks\": [\n" +
                            "      {\n" +
                            "        \"taskId\": \"1\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-12\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-14\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"9\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-15\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-16\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"3\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-17\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-19\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"12\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-20\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-22\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"22\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-23\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-25\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"25\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-26\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-28\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"userId\": \"2\",\n" +
                            "    \"userName\": \"jane@example.com\",\n" +
                            "    \"assignedTasks\": [\n" +
                            "      {\n" +
                            "        \"taskId\": \"11\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-12\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-13\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"15\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-14\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-15\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"29\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-16\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-17\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"30\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-18\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-19\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"32\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-20\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-21\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"userId\": \"3\",\n" +
                            "    \"userName\": \"admin@example.com\",\n" +
                            "    \"assignedTasks\": [\n" +
                            "      {\n" +
                            "        \"taskId\": \"7\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-12\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-13\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"14\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-14\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-15\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"taskId\": \"23\",\n" +
                            "        \"estimatedStartDate\": \"2025-05-16\",\n" +
                            "        \"estimatedEndDate\": \"2025-05-19\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }\n" +
                            "]",
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
        List<UserAssignmentDto> userAssignments = objectMapper.readValue(
                aiResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserAssignmentDto.class)
        );

        Map<String, TaskDto> taskMap = buildTaskMap(projectData);
        Map<Long, String> storyIdToTitleMap = buildStoryIdToTitleMap(projectData);
        Map<String, UserDto> userMap = buildUserMap(users);

        List<Map<String, Object>> result = new ArrayList<>();
        for (UserAssignmentDto assignment : userAssignments) {
            result.add(buildUserAssignmentWithStories(assignment, taskMap, storyIdToTitleMap, userMap));
        }

        return objectMapper.writeValueAsString(result);
    }

    private Map<String, TaskDto> buildTaskMap(List<Map<String, Object>> projectData) {
        return projectData.stream()
                .flatMap(story -> ((List<TaskDto>) story.get("tasks")).stream())
                .collect(Collectors.toMap(TaskDto::getId, task -> task));
    }

    private Map<Long, String> buildStoryIdToTitleMap(List<Map<String, Object>> projectData) {
        Map<Long, String> storyMap = new HashMap<>();
        for (Map<String, Object> story : projectData) {
            Long storyId = ((Number) story.get("id")).longValue();
            String storyTitle = (String) story.get("title");
            storyMap.put(storyId, storyTitle);
        }
        return storyMap;
    }

    private Map<String, UserDto> buildUserMap(List<UserDto> users) {
        return users.stream()
                .collect(Collectors.toMap(user -> String.valueOf(user.getId()), user -> user));
    }

    private Map<String, Object> buildUserAssignmentWithStories(UserAssignmentDto assignment, Map<String, TaskDto> taskMap, Map<Long, String> storyIdToTitleMap, Map<String, UserDto> userMap) {
        Map<String, Object> userResult = new HashMap<>();
        UserDto user = userMap.get(assignment.getUserId());
        userResult.put("userName", user != null ? user.getUsername() : "Unknown User");

        Map<String, List<Map<String, Object>>> storyToTasks = new HashMap<>();
        for (AssignedTaskDto assignedTask : assignment.getAssignedTasks()) {
            TaskDto task = taskMap.get(assignedTask.getTaskId());
            if (task != null) {
                String storyTitle = storyIdToTitleMap.get(task.getUserStoryId());
                if (storyTitle == null) {
                    storyTitle = "Unknown Story";
                }

                Map<String, Object> taskDetails = new HashMap<>();
                taskDetails.put("taskId", task.getId());
                taskDetails.put("title", task.getTitle());
                taskDetails.put("estimatedStartDate", assignedTask.getEstimatedStartDate());
                taskDetails.put("estimatedEndDate", assignedTask.getEstimatedEndDate());
                taskDetails.put("estimatedHours", task.getEstimatedHours());

                storyToTasks.computeIfAbsent(storyTitle, k -> new ArrayList<>()).add(taskDetails);
            }
        }

        List<Map<String, Object>> assignedStories = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : storyToTasks.entrySet()) {
            Map<String, Object> storyDetails = new HashMap<>();
            storyDetails.put("storyTitle", entry.getKey());
            storyDetails.put("tasks", entry.getValue());
            assignedStories.add(storyDetails);
        }

        userResult.put("assignedStories", assignedStories);
        return userResult;
    }

    @Override
    public List<UserDto> getUsersByProjectId(Long projectId) {
        return userClient.getUserByProjectId(projectId);
    }
}
