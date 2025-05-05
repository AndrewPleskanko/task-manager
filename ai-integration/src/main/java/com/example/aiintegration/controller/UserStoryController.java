package com.example.aiintegration.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aiintegration.dto.UserStoryDto;
import com.example.aiintegration.service.IUserStoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-stories")
public class UserStoryController {
    private final IUserStoryService userStoryService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<UserStoryDto>> getUserStoriesByProjectId(@PathVariable Long projectId) {
        log.info("Fetching user stories for projectId: {}", projectId);

        List<UserStoryDto> userStories = userStoryService.getUserStoriesByProjectId(projectId);

        if (userStories.isEmpty()) {
            log.warn("No user stories found for projectId: {}", projectId);
            return ResponseEntity.noContent().build();
        }

        log.info("Found {} user stories for projectId: {}", userStories.size(), projectId);
        return ResponseEntity.ok(userStories);
    }
}