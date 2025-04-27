package org.example.taskservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.taskservice.entity.Task;
import org.example.taskservice.enums.Priority;
import org.example.taskservice.enums.Status;
import org.example.taskservice.service.impl.TaskServiceImpl;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskServiceImpl taskService;

    @GetMapping
    public List<Task> getAllTasks(@RequestHeader("X-User-Id") String userIdString) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getAllTasks(userId);
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@RequestHeader("X-User-Id") String userIdString, @PathVariable String id) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getTaskById(id, userId);
    }

    @PostMapping
    public Task createTask(@RequestHeader("X-User-Id") String userIdString, @RequestBody Task task) {
        Long userId = Long.parseLong(userIdString);
        task.setUserId(userId);
        return taskService.createTask(task, userId);
    }

    @PutMapping("/{id}")
    public Task updateTask(@RequestHeader("X-User-Id") String userIdString, @PathVariable String id, @RequestBody Task taskDetails) {
        Long userId = Long.parseLong(userIdString);
        return taskService.updateTask(id, taskDetails, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@RequestHeader("X-User-Id") String userIdString, @PathVariable String id) {
        Long userId = Long.parseLong(userIdString);
        taskService.deleteTask(id, userId);
    }

    @GetMapping("/statuses")
    public List<Status> getStatuses() {
        return Arrays.asList(Status.values());
    }

    @GetMapping("/priorities")
    public List<Priority> getPriorities() {
        return Arrays.asList(Priority.values());
    }

    @GetMapping("/chart/status")
    public Map<String, Long> getTaskCountByStatus(@RequestHeader("X-User-Id") String userIdString) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getTaskCountByStatus(userId);
    }

    @GetMapping("/chart/priority")
    public Map<String, Long> getTaskCountByPriority(@RequestHeader("X-User-Id") String userIdString) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getTaskCountByPriority(userId);
    }

    @GetMapping("/chart/users")
    public Map<Long, Long> getTaskCountByUser(@RequestHeader("X-User-Id") String userIdString) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getTaskCountByUser(userId);
    }

    @GetMapping("/chart/daily")
    public Map<String, Long> getTaskCountByDay(@RequestHeader("X-User-Id") String userIdString) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getTaskCountByDay(userId);
    }

    @GetMapping("/chart/completed")
    public Map<String, Long> getCompletedTaskCountByDay(@RequestHeader("X-User-Id") String userIdString) {
        Long userId = Long.parseLong(userIdString);
        return taskService.getCompletedTaskCountByDay(userId);
    }

    @GetMapping("/user-story/{userStoryId}/active")
    public List<Task> getActiveTasksByUserStoryId(@PathVariable Long userStoryId) {
        return taskService.getActiveTasksByUserStoryId(userStoryId);
    }
}