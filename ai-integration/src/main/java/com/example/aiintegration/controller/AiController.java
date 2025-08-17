package com.example.aiintegration.controller;

import java.util.List;
import java.util.Map;

import com.example.aiintegration.service.impl.AiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aiintegration.service.IProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ai")
class AiController {

    private final IProjectService projectService;
    private final AiService aiService;

    @PostMapping("/process/{projectId}")
    public ResponseEntity<String> processProjectData(@PathVariable Long projectId) {
        try {
            String aiResponse = projectService.processDataWithAi(projectId);
            return ResponseEntity.ok(aiResponse);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request for projectId {}: {}", projectId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error processing data with AI for projectId {}: {}", projectId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process data with AI service: " + e.getMessage());
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<Map<String, Object>>> getUserStoriesByProjectId(@PathVariable Long projectId) {
        log.info("Fetching user stories for projectId: {}", projectId);

        List<Map<String, Object>> userStories = projectService.getUserStoriesForAI(projectId);

        if (userStories == null || userStories.isEmpty()) {
            log.warn("No user stories found for projectId: {}", projectId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/project/{projectId}/redis")
    public ResponseEntity<String> getAiProjectRedisData(@PathVariable Long projectId) {
        String data = aiService.getAiProjectData(projectId);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}