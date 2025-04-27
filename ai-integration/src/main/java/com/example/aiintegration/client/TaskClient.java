package com.example.aiintegration.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.aiintegration.dto.TaskDto;

@FeignClient(name = "task-service")
public interface TaskClient {

    @GetMapping("/api/v1/tasks/user-story/{userStoryId}/active")
    List<TaskDto> getActiveTasksByUserStoryId(@PathVariable Long userStoryId);
}


