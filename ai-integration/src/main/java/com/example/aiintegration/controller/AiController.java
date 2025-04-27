package com.example.aiintegration.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aiintegration.service.impl.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ai")
class AiController {

    private final ProjectService projectService;

    @GetMapping("/process/{projectId}")
    public ResponseEntity<String> processDataWithAi(
            @RequestParam("prompt") String prompt,
            @PathVariable Long projectId) {

        log.info("Processing AI request for projectId: {} with prompt: {}", projectId, prompt);
        return projectService.processDataWithAi(projectId, prompt);
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
}