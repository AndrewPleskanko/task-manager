package org.example.taskservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.taskservice.entity.Task;
import org.example.taskservice.enums.Priority;
import org.example.taskservice.enums.Status;
import org.example.taskservice.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        return taskOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task taskDetails) {
        Optional<Task> existingTask = taskService.getTaskById(id);

        if (existingTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        Optional<Task> existingTask = taskService.getTaskById(id);

        if (existingTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable String id, @RequestBody String newStatus) {
        Optional<Task> existingTask = taskService.getTaskById(id);

        if (existingTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task updatedTask = taskService.updateTaskStatus(id, newStatus);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<Status>> getStatuses() {
        return ResponseEntity.ok(Arrays.asList(Status.values()));
    }

    @GetMapping("/priorities")
    public ResponseEntity<List<Priority>> getPriorities() {
        return ResponseEntity.ok(Arrays.asList(Priority.values()));
    }

    @GetMapping("/chart/status")
    public ResponseEntity<Map<String, Long>> getTaskCountByStatus() {
        return ResponseEntity.ok(taskService.getTaskCountByStatus());
    }

    @GetMapping("/chart/priority")
    public ResponseEntity<Map<String, Long>> getTaskCountByPriority() {
        return ResponseEntity.ok(taskService.getTaskCountByPriority());
    }

    @GetMapping("/chart/users")
    public ResponseEntity<Map<Long, Long>> getTaskCountByUser() {
        return ResponseEntity.ok(taskService.getTaskCountByUser());
    }

    @GetMapping("/chart/daily")
    public ResponseEntity<Map<String, Long>> getTaskCountByDay() {
        return ResponseEntity.ok(taskService.getTaskCountByDay());
    }

    @GetMapping("/chart/completed")
    public ResponseEntity<Map<String, Long>> getCompletedTaskCountByDay() {
        return ResponseEntity.ok(taskService.getCompletedTaskCountByDay());
    }

    @GetMapping("/user-story/{userStoryId}/active")
    public ResponseEntity<List<Task>> getActiveTasksByUserStoryId(@PathVariable Long userStoryId) {
        List<Task> activeTasks = taskService.getActiveTasksByUserStoryId(userStoryId);
        return ResponseEntity.ok(activeTasks);
    }
}